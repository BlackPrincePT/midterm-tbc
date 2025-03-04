package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import javax.inject.Inject

class FindEquipment @Inject constructor(private val equipmentRepository: EquipmentRepository) {
    operator fun invoke(owner: String, sharedShiftId: String) =
        equipmentRepository.findEquipment(owner, sharedShiftId)
}