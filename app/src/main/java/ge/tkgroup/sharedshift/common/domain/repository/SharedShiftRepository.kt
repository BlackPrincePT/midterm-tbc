package ge.tkgroup.sharedshift.common.domain.repository

import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface SharedShiftRepository {
    suspend fun fetchSharedShiftById(id: String): Resource<SharedShift>
    fun fetchSharedShift(id: String): Flow<SharedShift?>
    fun fetchMySharedShifts(userid: String): Flow<List<SharedShift>>
    fun createSharedShift(companies: List<String>, currentUserId: String)
    fun updateUserPermissions(sharedShiftId: String, userid: String, permissions: List<Permission>)

    fun addUser(userId: String, sharedShiftId: String)
    fun deleteUser(userId: String, sharedShiftId: String)
}