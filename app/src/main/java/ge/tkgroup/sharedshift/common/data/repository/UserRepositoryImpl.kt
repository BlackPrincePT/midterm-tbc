package ge.tkgroup.sharedshift.common.data.repository

import androidx.paging.PagingSource
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.USERNAME
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.USERS
import ge.tkgroup.sharedshift.common.data.remote.FirestorePagingSource
import ge.tkgroup.sharedshift.common.data.remote.FirestoreUtils
import ge.tkgroup.sharedshift.common.data.remote.model.UserDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.UserDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.domain.repository.UserRepository
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDtoMapper: UserDtoMapper,
    private val firestoreUtils: FirestoreUtils
) : UserRepository {

    private val db = Firebase.firestore

    override fun saveUser(user: User) {
        val userDto = userDtoMapper.mapFromDomain(user)
        db.collection(USERS).document(user.id).set(userDto)
    }

    override fun fetchUser(id: String): Flow<User?> {
        val documentRef = db.collection(USERS).document(id)

        return documentRef.snapshots()
            .map { documentSnapshot ->
                documentSnapshot.toObject(UserDto::class.java)?.let {
                    userDtoMapper.mapToDomain(it)
                }
            }
    }

    override suspend fun fetchUserById(id: String): Resource<User> {
        val documentRef = db.collection(USERS).document(id)

        return when (val result = firestoreUtils.readDocument<UserDto>(documentRef)) {
            is Resource.Success -> {
                val user = userDtoMapper.mapToDomain(result.data)
                Resource.Success(data = user)
            }

            is Resource.Error -> result
        }
    }

    override suspend fun fetchUserByUsername(username: String): Resource<User> {
        val query = db.collection(USERS).whereEqualTo(USERNAME, username)

        return when (val result = firestoreUtils.queryDocuments<UserDto>(query)) {
            is Resource.Success -> {
                val fetchedUsers = result.data.map(userDtoMapper::mapToDomain)

                // Usernames are unique, therefore return first
                if (fetchedUsers.isNotEmpty())
                    Resource.Success(data = fetchedUsers.first())
                else
                    Resource.Error(message = "Can't find user with such username")
            }

            is Resource.Error -> result
        }
    }

    // ============ Pagination ============ \\

    override fun getUsersPagingSource(userIds: List<String>): PagingSource<QuerySnapshot, User> {
        val query = db.collection(USERS)
            .whereIn(FieldPath.documentId(), userIds)

        return getUsersPagingSource(query)
    }

    private fun getUsersPagingSource(query: Query): PagingSource<QuerySnapshot, User> {
        return FirestorePagingSource(
            query = query,
            classInfo = UserDto::class.java,
            dtoMapper = userDtoMapper
        )
    }
}