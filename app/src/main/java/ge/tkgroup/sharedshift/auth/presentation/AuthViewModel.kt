package ge.tkgroup.sharedshift.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.HasSavedAuthSession
import ge.tkgroup.sharedshift.auth.domain.usecases.LaunchGoogleAuthOptions
import ge.tkgroup.sharedshift.common.utils.Resource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val launchGoogleAuthOptions: LaunchGoogleAuthOptions,
    val hasSavedAuthSession: HasSavedAuthSession
) : ViewModel() {

    val state = MutableSharedFlow<String>()

    fun button(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val result = launchGoogleAuthOptions()

            if (result is Resource.Error)
                state.emit(result.message)
            else {
                onSuccess.invoke()
            }
        }
    }
}