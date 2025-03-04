package ge.tkgroup.sharedshift.common.data.repository

import androidx.paging.PagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.EQUIPMENT
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.OWNER
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.SHARED_SHIFTS
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.USERS
import ge.tkgroup.sharedshift.common.data.remote.FirestorePagingSource
import ge.tkgroup.sharedshift.common.data.remote.FirestoreUtils
import ge.tkgroup.sharedshift.common.data.remote.model.EquipmentDto
import ge.tkgroup.sharedshift.common.data.remote.model.UserDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.EquipmentDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.domain.repository.EquipmentRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EquipmentRepositoryImpl @Inject constructor(
    private val equipmentDtoMapper: EquipmentDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : EquipmentRepository {

    private val db = Firebase.firestore

    override fun saveEquipment(equipment: Equipment, sharedShiftId: String) {
        val equipmentDto = equipmentDtoMapper.mapFromDomain(equipment)

        db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EQUIPMENT)
            .add(equipmentDto)
    }

    override suspend fun fetchEquipmentById(id: String, sharedShiftId: String): Resource<Equipment> {
        val documentRef = db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EQUIPMENT)
            .document(id)

        return when (val result = firestoreUtils.readDocument<EquipmentDto>(documentRef)) {
            is Resource.Success -> {
                val user = equipmentDtoMapper.mapToDomain(result.data)
                Resource.Success(data = user)
            }

            is Resource.Error -> result
        }
    }

    override fun findEquipment(owner: String, sharedShiftId: String): Flow<List<Equipment>> {
        val query = db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EQUIPMENT)
            .whereGreaterThanOrEqualTo(OWNER, owner)
            .whereLessThanOrEqualTo(OWNER, owner + "\uF7FF")

        return query.snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(EquipmentDto::class.java)
                    .map(equipmentDtoMapper::mapToDomain)
            }
    }

    // ============ Pagination ============ \\

    override fun getEquipmentPagingSource(sharedShiftId: String): PagingSource<QuerySnapshot, Equipment> {
        val collectionRef = db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(EQUIPMENT)

        return getEmployeesPagingSource(collectionRef)
    }

    private fun getEmployeesPagingSource(query: Query): PagingSource<QuerySnapshot, Equipment> {
        return FirestorePagingSource(
            query = query,
            classInfo = EquipmentDto::class.java,
            dtoMapper = equipmentDtoMapper
        )
    }
}