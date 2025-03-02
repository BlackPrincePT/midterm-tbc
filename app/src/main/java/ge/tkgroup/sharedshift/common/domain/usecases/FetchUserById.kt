package ge.tkgroup.sharedshift.common.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import javax.inject.Inject

class FetchUserById @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: String) = userRepository.fetchUserById(id)
}