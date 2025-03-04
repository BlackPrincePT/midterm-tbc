package ge.tkgroup.sharedshift.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.CacheAuthState
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.auth.domain.usecases.SaveUserData
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.utils.UserNotAuthenticatedException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val saveUserData: SaveUserData,
    private val getCurrentUserId: GetCurrentUserId,
    private val cacheAuthState: CacheAuthState
) : ViewModel() {

    fun uploadUserData(username: String) = viewModelScope.launch {
        val currentUserId = getCurrentUserId() ?: throw UserNotAuthenticatedException()
        val user = User(id = currentUserId, username = username)
        saveUserData(user)
        cacheAuthState(state = true)
    }
}
