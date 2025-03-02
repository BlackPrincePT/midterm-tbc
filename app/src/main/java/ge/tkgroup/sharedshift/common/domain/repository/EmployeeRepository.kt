package ge.tkgroup.sharedshift.common.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.Employee

interface EmployeeRepository {

    fun getEmployeesPagingSource(sharedShiftId: String): PagingSource<QuerySnapshot, Employee>
}