package ge.tkgroup.sharedshift.common.data.remote.model.mappers

import ge.tkgroup.sharedshift.common.data.remote.model.EquipmentDto
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.utils.MappingException
import javax.inject.Inject

class EquipmentDtoMapper @Inject constructor() : DtoMapper<EquipmentDto, Equipment> {

    override fun mapToDomain(data: EquipmentDto): Equipment {
        return Equipment(
            id = data.id ?: throw MappingException("Equipment ID cannot be null"),
            manufacturer = data.manufacturer,
            model = data.model,
            licensePlate = data.licensePlate,
            owner = data.owner
        )
    }

    override fun mapFromDomain(data: Equipment): EquipmentDto {
        return EquipmentDto(
            id = data.id.ifEmpty { null },
            manufacturer = data.manufacturer,
            model = data.model,
            licensePlate = data.licensePlate,
            owner = data.owner
        )
    }
}