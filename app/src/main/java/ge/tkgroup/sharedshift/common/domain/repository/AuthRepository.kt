package ge.tkgroup.sharedshift.common.domain.repository

import android.content.Context
import ge.tkgroup.sharedshift.common.utils.Resource

interface AuthRepository {
    fun hasSavedAuthSession(): Boolean
    fun getCurrentUserId(): String?
    suspend fun launchGoogleAuthOptions(context: Context): Resource.Error?
    fun signOut()

    suspend fun cacheAuthState(state: Boolean)
    suspend fun readCachedAuthState(): Boolean?
    suspend fun clearCachedAuthState()
}