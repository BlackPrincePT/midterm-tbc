package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import javax.inject.Inject

class ClearCachedAuthState @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.clearCachedAuthState()
}