package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import ge.tkgroup.sharedshift.common.utils.UserNotAuthenticatedException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCurrentUser @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User?> {
        val currentUserId =
            authRepository.getCurrentUserId() ?: throw UserNotAuthenticatedException()

        return userRepository.fetchUser(currentUserId)
    }
}