package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LaunchGoogleAuthOptions @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        authRepository.launchGoogleAuthOptions()
    }
}