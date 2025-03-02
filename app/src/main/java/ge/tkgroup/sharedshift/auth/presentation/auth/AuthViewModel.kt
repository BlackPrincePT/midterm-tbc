package ge.tkgroup.sharedshift.auth.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.auth.domain.usecases.HasSavedAuthSession
import ge.tkgroup.sharedshift.auth.domain.usecases.LaunchGoogleAuthOptions
import ge.tkgroup.sharedshift.common.domain.usecases.FetchUserById
import ge.tkgroup.sharedshift.common.presentation.Event
import ge.tkgroup.sharedshift.common.utils.Resource
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
) : ViewModel() {

    private val _state = MutableStateFlow(AuthViewState())
    val state: StateFlow<AuthViewState>
        get() = _state

    fun authenticate() = viewModelScope.launch {
        setLoading()
        val authResult = launchGoogleAuthOptions()

        if (authResult is Resource.Error)
            _state.update { oldState ->
                oldState.copy(isLoading = false, errorMsg = Event(authResult.message))
            }
        else
            onAuthSuccess()
    }

    fun checkForSavedAuthState() = viewModelScope.launch {
        if (hasSavedAuthSession()) {
            setLoading()
            onAuthSuccess()
        }
    }

    private suspend fun onAuthSuccess() {
        val currentUserId = getCurrentUserId() ?: return
        val isRegistrationComplete = fetchUserById(id = currentUserId) is Resource.Success
        _state.update { oldState ->
            oldState.copy(
                isLoading = false,
                isAuthenticated = true,
                isRegistrationComplete = isRegistrationComplete
            )
        }
    }

    private fun setLoading(isLoading: Boolean = true) {
        _state.update { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }
}