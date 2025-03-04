package ge.tkgroup.sharedshift.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.settings.domain.usecases.ClearActiveSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.ClearCachedAuthState
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchCurrentUser
import ge.tkgroup.sharedshift.settings.domain.usecases.SignUserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val fetchCurrentUser: FetchCurrentUser,
    private val signUserOut: SignUserOut,
    private val clearCachedAuthState: ClearCachedAuthState,
    private val clearActiveSharedShift: ClearActiveSharedShift
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsViewState())
    val state = _state.asStateFlow()

    val currentUser = fetchCurrentUser()
        .filterNotNull()

    fun signOut() = viewModelScope.launch {
        signUserOut()
        clearCachedAuthState()
        clearActiveSharedShift()

        _state.update { oldState ->
            oldState.copy(shouldSignOut = true)
        }
    }
}