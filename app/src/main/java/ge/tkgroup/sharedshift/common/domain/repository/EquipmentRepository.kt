package ge.tkgroup.sharedshift.common.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface EquipmentRepository {
    fun findEquipment(owner: String, sharedShiftId: String): Flow<List<Equipment>>
    suspend fun fetchEquipmentById(id: String, sharedShiftId: String): Resource<Equipment>
    fun saveEquipment(equipment: Equipment, sharedShiftId: String)
    fun getEquipmentPagingSource(sharedShiftId: String): PagingSource<QuerySnapshot, Equipment>
}