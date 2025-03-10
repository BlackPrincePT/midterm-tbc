package ge.tkgroup.sharedshift.common.data.remote.model.mappers

import ge.tkgroup.sharedshift.common.data.remote.model.WorkDayDto
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.utils.MappingException
import ge.tkgroup.sharedshift.common.utils.extensions.toLocalDate
import ge.tkgroup.sharedshift.common.utils.extensions.toTimestamp
import javax.inject.Inject

class WorkDayDtoMapper @Inject constructor() : DtoMapper<WorkDayDto, WorkDay> {

    override fun mapToDomain(data: WorkDayDto): WorkDay {
        return WorkDay(
            id = data.id ?: throw MappingException("WorkDay ID cannot be null"),
            employeeId = data.employeeId,
            hoursWorked = data.hoursWorked,
            exclusiveShift = data.exclusiveShift,
            date = data.timestamp.toLocalDate(),
            notes = data.notes
        )
    }

    override fun mapFromDomain(data: WorkDay): WorkDayDto {
        return WorkDayDto(
            id = data.id.ifEmpty { null },
            employeeId = data.employeeId,
            hoursWorked = data.hoursWorked,
            exclusiveShift = data.exclusiveShift,
            timestamp = data.date.toTimestamp(),
            notes = data.notes
        )
    }
}