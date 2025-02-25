package ge.tkgroup.sharedshift.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.auth.domain.usecases.SaveUserData
import ge.tkgroup.sharedshift.common.domain.model.User
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val saveUserData: SaveUserData,
    private val getCurrentUserId: GetCurrentUserId
) : ViewModel() {

    fun uploadUserData(username: String): Boolean = getCurrentUserId()?.let {
        val user = User(id = it, username = username)
        saveUserData(user)
        true
    } ?: false
}
