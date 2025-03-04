package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import javax.inject.Inject

class CacheAuthState @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(state: Boolean) = authRepository.cacheAuthState(state)
}