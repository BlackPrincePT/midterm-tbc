package ge.tkgroup.sharedshift.equipment.model.usecases

import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.ActiveSharedShiftNotFoundException
import javax.inject.Inject

class CreateEquipment @Inject constructor(
    private val equipmentRepository: EquipmentRepository,
    private val sharedShiftRepository: SharedShiftRepository
) {
    suspend operator fun invoke(equipment: Equipment) {
        val sharedShiftId = sharedShiftRepository.readCachedActiveSharedShift()
            ?: throw ActiveSharedShiftNotFoundException()

        equipmentRepository.saveEquipment(equipment, sharedShiftId)
    }
}