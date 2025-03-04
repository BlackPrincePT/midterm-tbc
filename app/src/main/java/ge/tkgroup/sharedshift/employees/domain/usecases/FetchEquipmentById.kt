package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.ActiveSharedShiftNotFoundException
import ge.tkgroup.sharedshift.common.utils.Resource
import javax.inject.Inject

class FetchEquipmentById @Inject constructor(
    private val equipmentRepository: EquipmentRepository,
    private val sharedShiftRepository: SharedShiftRepository
) {
    suspend operator fun invoke(equipmentId: String): Resource<Equipment> {
        val sharedShiftId = sharedShiftRepository.readCachedActiveSharedShift()
            ?: throw ActiveSharedShiftNotFoundException()

        return equipmentRepository.fetchEquipmentById(equipmentId, sharedShiftId)
    }
}