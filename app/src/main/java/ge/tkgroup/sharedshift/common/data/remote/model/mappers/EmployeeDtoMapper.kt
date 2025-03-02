package ge.tkgroup.sharedshift.common.data.remote.model.mappers

import ge.tkgroup.sharedshift.common.utils.MappingException
import ge.tkgroup.sharedshift.common.data.remote.model.EmployeeDto
import ge.tkgroup.sharedshift.common.domain.model.Employee
import javax.inject.Inject

class EmployeeDtoMapper @Inject constructor() : DtoMapper<EmployeeDto, Employee> {

    override fun mapToDomain(data: EmployeeDto): Employee {
        return Employee(
            id = data.id ?: throw MappingException("Employee ID cannot be null"),
            firstName = data.firstName,
            lastName = data.lastName,
            personalNumber = data.personalNumber,
            occupation = data.occupation,
            iban = data.iban,
            salaryInGEL = data.salaryInGEL,
            hasPensionTaxDeduction = data.hasPensionTaxDeduction
        )
    }

    override fun mapFromDomain(data: Employee): EmployeeDto {
        return EmployeeDto(
            id = data.id,
            firstName = data.firstName,
            lastName = data.lastName,
            personalNumber = data.personalNumber,
            occupation = data.occupation,
            iban = data.iban,
            salaryInGEL = data.salaryInGEL,
            hasPensionTaxDeduction = data.hasPensionTaxDeduction
        )
    }
}