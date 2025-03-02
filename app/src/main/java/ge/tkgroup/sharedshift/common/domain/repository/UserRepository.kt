package ge.tkgroup.sharedshift.common.domain.repository

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun fetchUserById(id: String): Resource<User>
    suspend fun fetchUserByUsername(username: String): Resource<User>

    fun fetchUser(id: String): Flow<User?>
    fun saveUser(user: User)

    fun getUsersPagingSource(userIds: List<String>): PagingSource<QuerySnapshot, User>
}