package ge.tkgroup.sharedshift.common.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetCurrentUserData @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(scope: CoroutineScope): StateFlow<User?> {
        return authRepository.getCurrentUser()
            .stateIn(
                scope,
                SharingStarted.Eagerly,
                initialValue = null
            )
    }
}