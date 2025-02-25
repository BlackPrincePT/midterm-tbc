package ge.tkgroup.sharedshift.common.data.repository

import android.content.Context
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.tkgroup.sharedshift.BuildConfig
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.data.remote.model.UserDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.UserDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USERS = "users"

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userDtoMapper: UserDtoMapper
) : AuthRepository {

    private val db = Firebase.firestore

    override fun saveUserData(user: User) {
        val userDto = userDtoMapper.mapFromDomain(user)
        db.collection(USERS).document(user.id).set(userDto)
    }

    override fun getCurrentUser(): Flow<User?> = currentUserData
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid
    override fun hasSavedAuthSession(): Boolean = FirebaseAuth.getInstance().currentUser != null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentUserData by lazy {
        currentUserId
            .filterNotNull()
            .flatMapLatest { userId ->
                fetchCurrentUserData(userId)
            }
    }

    private fun fetchCurrentUserData(userId: String): Flow<User?> {
        val documentRef = db.collection(USERS).document(userId)

        return documentRef.snapshots()
            .map { documentSnapshot ->
                documentSnapshot.toObject(UserDto::class.java)?.let {
                    userDtoMapper.mapToDomain(it)
                }
            }
    }

    private val currentUserId: Flow<String?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            this.trySend(auth.currentUser?.uid)
        }
        Firebase.auth.addAuthStateListener(listener)

        trySend(FirebaseAuth.getInstance().currentUser?.uid)

        awaitClose { Firebase.auth.removeAuthStateListener(listener) }
    }

    override suspend fun launchGoogleAuthOptions(): Resource.Error? {
        return try {
            val signInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(BuildConfig.DEFAULT_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = CredentialManager.create(context).getCredential(
                request = request,
                context = context
            )

            createTokenWithCredentials(result.credential)

        } catch (e: NoCredentialException) {
            Resource.Error(message = context.getString(R.string.no_google_accounts_found_on_this_device))
        } catch (e: GetCredentialException) {
            Resource.Error(message = context.getString(R.string.something_went_wrong))
        }
    }

    private suspend fun createTokenWithCredentials(credential: Credential): Resource.Error? {
        return if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            signInWithGoogle(googleIdTokenCredential.idToken)
            null
        } else {
            Resource.Error(message = context.getString(R.string.something_went_wrong))
        }
    }

    private suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }
}