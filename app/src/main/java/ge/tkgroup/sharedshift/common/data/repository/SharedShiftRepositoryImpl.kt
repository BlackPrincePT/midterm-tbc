package ge.tkgroup.sharedshift.common.data.repository

import androidx.paging.PagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import ge.tkgroup.sharedshift.common.data.local.DataStoreConstants.SHARED_SHIFT_KEY
import ge.tkgroup.sharedshift.common.data.local.DataStoreManager
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.PERMISSIONS
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.SHARED_SHIFTS
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.USERS
import ge.tkgroup.sharedshift.common.data.remote.FirestorePagingSource
import ge.tkgroup.sharedshift.common.data.remote.FirestoreUtils
import ge.tkgroup.sharedshift.common.data.remote.model.SharedShiftDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.SharedShiftDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SharedShiftRepositoryImpl @Inject constructor(
    private val sharedShiftDtoMapper: SharedShiftDtoMapper,
    private val dataStoreManager: DataStoreManager,
    private val firestoreUtils: FirestoreUtils
) : SharedShiftRepository {

    private val db = Firebase.firestore

    override fun observeCachedActiveSharedShift(): Flow<String> {
        return dataStoreManager.observeData(SHARED_SHIFT_KEY)
            .filterNotNull()
    }

    override suspend fun readCachedActiveSharedShift(): String? {
        return dataStoreManager.readData(SHARED_SHIFT_KEY)
    }

    override suspend fun cacheActiveSharedShift(id: String) {
        dataStoreManager.saveData(SHARED_SHIFT_KEY, id)
    }

    override suspend fun clearCachedActiveShift() {
        dataStoreManager.deleteData(SHARED_SHIFT_KEY)
    }

    override suspend fun fetchSharedShiftById(id: String): Resource<SharedShift> {
        val documentRef = db.collection(SHARED_SHIFTS).document(id)

        return when (val result = firestoreUtils.readDocument<SharedShiftDto>(documentRef)) {
            is Resource.Success -> {
                val user = sharedShiftDtoMapper.mapToDomain(result.data)
                Resource.Success(data = user)
            }

            is Resource.Error -> result
        }
    }

    override fun fetchSharedShift(id: String): Flow<SharedShift?> {
        return db.collection(SHARED_SHIFTS).document(id).snapshots()
            .map { documentSnapshot ->
                documentSnapshot.toObject(SharedShiftDto::class.java)?.let {
                    sharedShiftDtoMapper.mapToDomain(it)
                }
            }
    }

    override fun fetchMySharedShifts(userid: String): PagingSource<QuerySnapshot, SharedShift> {
        val query = db.collection(SHARED_SHIFTS)
            .whereArrayContains(USERS, userid)

        return getUsersPagingSource(query)
    }

    private fun getUsersPagingSource(query: Query): PagingSource<QuerySnapshot, SharedShift> {
        return FirestorePagingSource(
            query = query,
            classInfo = SharedShiftDto::class.java,
            dtoMapper = sharedShiftDtoMapper
        )
    }

    override fun createSharedShift(companies: List<String>, currentUserId: String) {
        val godPermissions = Permission.entries.map(Permission::name)

        val sharedShiftDto = SharedShiftDto(
            companies = companies,
            users = listOf(currentUserId),
            permissions = mapOf(currentUserId to godPermissions)
        )

        db.collection(SHARED_SHIFTS).add(sharedShiftDto)
    }

    override fun updateUserPermissions(
        sharedShiftId: String,
        userid: String,
        permissions: List<Permission>
    ) {
        val stringPermissions = permissions.map(Permission::name)

        db.collection(SHARED_SHIFTS).document(sharedShiftId)
            .update("$PERMISSIONS.$userid", stringPermissions)
    }

    override fun addUser(userId: String, sharedShiftId: String) {
        db.collection(SHARED_SHIFTS).document(sharedShiftId)
            .update(USERS, FieldValue.arrayUnion(userId))
    }

    override fun deleteUser(userId: String, sharedShiftId: String) {
        db.collection(SHARED_SHIFTS).document(sharedShiftId).apply {
            update(USERS, FieldValue.arrayRemove(userId))
            update("$PERMISSIONS.$userId", FieldValue.delete())
        }
    }
}