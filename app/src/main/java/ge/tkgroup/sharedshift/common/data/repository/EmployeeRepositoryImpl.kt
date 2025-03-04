package ge.tkgroup.sharedshift.common.data.repository

import androidx.paging.PagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.EMPLOYEES
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.SHARED_SHIFTS
import ge.tkgroup.sharedshift.common.data.remote.FirestorePagingSource
import ge.tkgroup.sharedshift.common.data.remote.FirestoreUtils
import ge.tkgroup.sharedshift.common.data.remote.model.EmployeeDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.EmployeeDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.domain.repository.EmployeeRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeDtoMapper: EmployeeDtoMapper,
    private val firestoreUtils: FirestoreUtils
): EmployeeRepository {

    private val db = Firebase.firestore

    override fun saveEmployee(employee: Employee, sharedShiftId: String) {
        val employeeDto = employeeDtoMapper.mapFromDomain(employee)

        db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EMPLOYEES)
            .add(employeeDto)
    }

    override suspend fun fetchEmployeeById(id: String, sharedShiftId: String): Resource<Employee> {
        val documentRef = db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EMPLOYEES)
            .document(id)

        return when(val result = firestoreUtils.readDocument<EmployeeDto>(documentRef)) {
            is Resource.Success -> {
                val employee = employeeDtoMapper.mapToDomain(result.data)

                Resource.Success(data = employee)
            }

            is Resource.Error -> result
        }
    }

    // ============ Pagination ============ \\

    override fun getEmployeesPagingSource(sharedShiftId: String): PagingSource<QuerySnapshot, Employee> {
        val collectionRef = db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EMPLOYEES)

        return getEmployeesPagingSource(collectionRef)
    }

    private fun getEmployeesPagingSource(query: Query): PagingSource<QuerySnapshot, Employee> {
        return FirestorePagingSource(
            query = query,
            classInfo = EmployeeDto::class.java,
            dtoMapper = employeeDtoMapper
        )
    }
}