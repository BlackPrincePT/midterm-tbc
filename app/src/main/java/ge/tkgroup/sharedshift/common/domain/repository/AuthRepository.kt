package ge.tkgroup.sharedshift.common.domain.repository

import ge.tkgroup.sharedshift.common.utils.Resource

interface AuthRepository {
    fun hasSavedAuthSession(): Boolean
    fun getCurrentUserId(): String?
    suspend fun launchGoogleAuthOptions(): Resource.Error?
    fun signOut()
}