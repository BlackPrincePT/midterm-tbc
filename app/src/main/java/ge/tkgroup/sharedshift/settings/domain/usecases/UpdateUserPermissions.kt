package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class UpdateUserPermissions @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    operator fun invoke(sharedShiftId: String, userid: String, permissions: List<Permission>) {
        sharedShiftRepository.updateUserPermissions(
            sharedShiftId = sharedShiftId,
            userid = userid,
            permissions = permissions
        )
    }
}