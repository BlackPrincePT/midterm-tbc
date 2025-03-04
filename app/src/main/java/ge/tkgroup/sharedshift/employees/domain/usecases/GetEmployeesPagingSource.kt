package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import javax.inject.Inject

class GetEmployeesPagingSource @Inject constructor(private val employeeRepository: EmployeeRepository) {
    operator fun invoke(sharedShiftId: String) = employeeRepository.getEmployeesPagingSource(sharedShiftId)
}