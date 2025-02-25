package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import javax.inject.Inject

class SaveUserData @Inject constructor(
    private val userRepository: AuthRepository
) {
    operator fun invoke(user: User) = userRepository.saveUserData(user)
}