package ge.tkgroup.sharedshift.common.data.repository

import android.content.Context
import androidx.core.content.ContextCompat.getString
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
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.tkgroup.sharedshift.BuildConfig
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.data.local.DataStoreConstants.AUTH_STATE_KEY
import ge.tkgroup.sharedshift.common.data.local.DataStoreManager
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override fun hasSavedAuthSession(): Boolean = FirebaseAuth.getInstance().currentUser != null
    override fun getCurrentUserId(): String? = Firebase.auth.currentUser?.uid

    override suspend fun launchGoogleAuthOptions(context: Context): Resource.Error? {
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
            Resource.Error(message = e.message.toString())
        }
    }

    private suspend fun createTokenWithCredentials(credential: Credential): Resource.Error? {
        return if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            signInWithGoogle(googleIdTokenCredential.idToken)
            null
        } else {
            Resource.Error(message = "Something went wrong")
        }
    }

    private suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()
    }

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun cacheAuthState(state: Boolean) {
        dataStoreManager.saveData(AUTH_STATE_KEY, state)
    }

    override suspend fun readCachedAuthState(): Boolean? {
        return dataStoreManager.readData(AUTH_STATE_KEY)
    }

    override suspend fun clearCachedAuthState() {
        dataStoreManager.deleteData(AUTH_STATE_KEY)
    }
}