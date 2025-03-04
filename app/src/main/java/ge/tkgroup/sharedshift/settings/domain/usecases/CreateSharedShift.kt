package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.UserNotAuthenticatedException
import javax.inject.Inject

class CreateSharedShift @Inject constructor(
    private val sharedShiftRepository: SharedShiftRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(companies: List<String>) {
        val userId = authRepository.getCurrentUserId() ?: throw UserNotAuthenticatedException()

        sharedShiftRepository.createSharedShift(companies, userId)
    }
}