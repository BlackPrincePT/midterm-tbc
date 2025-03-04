package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.ActiveSharedShiftNotFoundException
import javax.inject.Inject

class CreateEmployee @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val sharedShiftRepository: SharedShiftRepository
) {
    suspend operator fun invoke(employee: Employee) {
        val sharedShiftId = sharedShiftRepository.readCachedActiveSharedShift()
            ?: throw ActiveSharedShiftNotFoundException()

        employeeRepository.saveEmployee(employee, sharedShiftId)
    }
}