package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersPagingSource @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userIds: List<String>) =
        userRepository.getUsersPagingSource(userIds)
}