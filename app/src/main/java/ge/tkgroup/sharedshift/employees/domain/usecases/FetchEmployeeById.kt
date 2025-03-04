package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.ActiveSharedShiftNotFoundException
import ge.tkgroup.sharedshift.common.utils.Resource
import javax.inject.Inject

class FetchEmployeeById @Inject constructor(
    private val sharedShiftRepository: SharedShiftRepository,
    private val employeeRepository: EmployeeRepository
) {
    suspend operator fun invoke(employeeId: String): Resource<Employee> {
        val activeSharedShiftId = sharedShiftRepository.readCachedActiveSharedShift()
            ?: throw ActiveSharedShiftNotFoundException()

        return employeeRepository.fetchEmployeeById(employeeId, activeSharedShiftId)
    }
}