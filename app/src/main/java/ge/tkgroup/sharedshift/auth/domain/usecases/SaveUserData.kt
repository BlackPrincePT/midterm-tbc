package ge.tkgroup.sharedshift.auth.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserData @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(user: User) = userRepository.saveUser(user)
}