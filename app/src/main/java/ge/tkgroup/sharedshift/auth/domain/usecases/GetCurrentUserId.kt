package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetCurrentUserId @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getCurrentUserId()
}