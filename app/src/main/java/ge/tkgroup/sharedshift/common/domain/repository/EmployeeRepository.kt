package ge.tkgroup.sharedshift.common.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.utils.Resource

interface EmployeeRepository {
    fun saveEmployee(employee: Employee, sharedShiftId: String)
    suspend fun fetchEmployeeById(id: String, sharedShiftId: String): Resource<Employee>
    fun getEmployeesPagingSource(sharedShiftId: String): PagingSource<QuerySnapshot, Employee>
}