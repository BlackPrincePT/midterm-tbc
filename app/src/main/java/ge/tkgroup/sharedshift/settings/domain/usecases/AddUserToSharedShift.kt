package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class AddUserToSharedShift @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    operator fun invoke(userId: String, sharedShiftId: String) {
        sharedShiftRepository.addUser(userId = userId, sharedShiftId = sharedShiftId)
    }
}