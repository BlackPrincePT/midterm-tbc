package ge.tkgroup.sharedshift.auth.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.CacheAuthState
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.auth.domain.usecases.HasSavedAuthSession
import ge.tkgroup.sharedshift.auth.domain.usecases.LaunchGoogleAuthOptions
import ge.tkgroup.sharedshift.auth.domain.usecases.ReadCachedAuthState
import ge.tkgroup.sharedshift.common.domain.usecases.FetchUserById
import ge.tkgroup.sharedshift.common.presentation.Event
import ge.tkgroup.sharedshift.common.utils.Resource
import ge.tkgroup.sharedshift.common.utils.UserNotAuthenticatedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val launchGoogleAuthOptions: LaunchGoogleAuthOptions,
    private val hasSavedAuthSession: HasSavedAuthSession,
    private val getCurrentUserId: GetCurrentUserId,
    private val fetchUserById: FetchUserById,
    private val readCachedAuthState: ReadCachedAuthState,
    private val cacheAuthState: CacheAuthState
) : ViewModel() {

    private val _state = MutableStateFlow(AuthViewState())
    val state: StateFlow<AuthViewState>
        get() = _state

    init {
        checkForSavedAuthState()
    }

    fun authenticate(context: Context) = viewModelScope.launch {
        setLoading()
        val authResult = launchGoogleAuthOptions(context)

        if (authResult !is Resource.Error)
            onAuthSuccess()
        else
            _state.update { oldState ->
                oldState.copy(isLoading = false, errorMsg = Event(authResult.message))
            }

    }

    private fun checkForSavedAuthState() = viewModelScope.launch {
        if (hasSavedAuthSession()) {
            setLoading()
            onAuthSuccess()
        }
    }

    private suspend fun onAuthSuccess() {
        val currentUserId = getCurrentUserId() ?: throw UserNotAuthenticatedException()
        val isRegistrationComplete = isRegistrationComplete(currentUserId)

        _state.update { oldState ->
            oldState.copy(
                isLoading = false,
                isAuthenticated = true,
                isRegistrationComplete = isRegistrationComplete
            )
        }
    }

    private suspend fun isRegistrationComplete(currentUserId: String): Boolean {
        if (readCachedAuthState() == true)
            return true

        val isComplete = fetchUserById(id = currentUserId) is Resource.Success
        cacheAuthState(isComplete)

        return isComplete
    }

    private fun setLoading(isLoading: Boolean = true) {
        _state.update { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }
}