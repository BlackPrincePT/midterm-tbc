package ge.tkgroup.sharedshift.auth.domain.usecases

import android.content.Context
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchGoogleAuthOptions @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(context: Context) = withContext(Dispatchers.IO) {
        authRepository.launchGoogleAuthOptions(context)
    }
}