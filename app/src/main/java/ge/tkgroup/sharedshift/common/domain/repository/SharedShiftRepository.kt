package ge.tkgroup.sharedshift.common.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SharedShiftRepository {
    fun observeCachedActiveSharedShift(): Flow<String>
    suspend fun readCachedActiveSharedShift(): String?
    suspend fun cacheActiveSharedShift(id: String)
    suspend fun clearCachedActiveShift()

    suspend fun fetchSharedShiftById(id: String): Resource<SharedShift>
    fun fetchSharedShift(id: String): Flow<SharedShift?>
    fun fetchMySharedShifts(userid: String): PagingSource<QuerySnapshot, SharedShift>

    fun createSharedShift(companies: List<String>, currentUserId: String)
    fun updateUserPermissions(sharedShiftId: String, userid: String, permissions: List<Permission>)
    fun addUser(userId: String, sharedShiftId: String)
    fun deleteUser(userId: String, sharedShiftId: String)
}