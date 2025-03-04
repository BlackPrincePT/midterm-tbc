package ge.tkgroup.sharedshift.equipment.model.usecases

import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import javax.inject.Inject

class GetEquipmentPagingSource @Inject constructor(private val equipmentRepository: EquipmentRepository) {
    operator fun invoke(sharedShiftId: String) =
        equipmentRepository.getEquipmentPagingSource(sharedShiftId)
}