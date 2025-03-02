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
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.tkgroup.sharedshift.BuildConfig
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.USERS
import ge.tkgroup.sharedshift.common.data.remote.FirestoreUtils
import ge.tkgroup.sharedshift.common.data.remote.model.UserDto
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.utils.DocumentNotFoundException
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    override fun hasSavedAuthSession(): Boolean = FirebaseAuth.getInstance().currentUser != null
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

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