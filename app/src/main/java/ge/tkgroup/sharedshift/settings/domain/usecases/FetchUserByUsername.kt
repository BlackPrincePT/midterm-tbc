package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserByUsername @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(username: String) = userRepository.fetchUserByUsername(username)
}