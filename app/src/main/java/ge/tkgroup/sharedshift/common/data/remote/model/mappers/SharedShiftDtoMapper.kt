package ge.tkgroup.sharedshift.common.data.remote.model.mappers

import ge.tkgroup.sharedshift.common.data.remote.model.SharedShiftDto
import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.utils.MappingException
import javax.inject.Inject

class SharedShiftDtoMapper @Inject constructor() : DtoMapper<SharedShiftDto, SharedShift> {

    override fun mapToDomain(data: SharedShiftDto): SharedShift {
        return SharedShift(
            id = data.id ?: throw MappingException("SharedShift ID cannot be null"),
            companies = data.companies,
            users = data.users,
            permissions = data.permissions.mapValues { it.value.map(Permission::valueOf) }
        )
    }

    override fun mapFromDomain(data: SharedShift): SharedShiftDto {
        return SharedShiftDto(
            id = data.id,
            companies = data.companies,
            users = data.users,
            permissions = data.permissions.mapValues { it.value.map(Permission::name) }
        )
    }
}