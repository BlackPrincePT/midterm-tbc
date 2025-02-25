package ge.tkgroup.sharedshift.common.domain.repository

import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun saveUserData(user: User)
    fun getCurrentUser(): Flow<User?>
    fun getCurrentUserId(): String?
    fun hasSavedAuthSession(): Boolean
    suspend fun launchGoogleAuthOptions(): Resource.Error?
    fun signOut()
}