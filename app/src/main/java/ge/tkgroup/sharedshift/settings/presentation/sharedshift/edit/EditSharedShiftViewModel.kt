package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.common.domain.model.User
import ge.tkgroup.sharedshift.common.presentation.Event
import ge.tkgroup.sharedshift.common.utils.Resource
import ge.tkgroup.sharedshift.settings.domain.usecases.AddUserToSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.DeleteUserFromSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.GetUsersPagingSource
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchUserByUsername
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditSharedShiftViewModel @Inject constructor(
    private val getUsersPagingSource: GetUsersPagingSource,
    private val fetchUserByUsername: FetchUserByUsername,
    private val addUserToSharedShift: AddUserToSharedShift,
    private val deleteUserFromSharedShift: DeleteUserFromSharedShift,
    fetchSharedShift: FetchSharedShift,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val args = EditSharedShiftFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _state = MutableStateFlow(EditSharedShiftViewState())
    val state = _state.asStateFlow()

    private val sharedShift = fetchSharedShift(id = args.sharedShiftId)
        .filterNotNull()

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedUsers = sharedShift
        .flatMapMerge {
            Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { getUsersPagingSource(it.users) }
            )
                .flow
                .cachedIn(viewModelScope)
        }

    fun findUserByUsername(username: String, onSuccess: (User) -> Unit) = viewModelScope.launch {
        setLoading(true)

        when (val result = fetchUserByUsername(username)) {
            is Resource.Success -> {
                _state.update { oldState ->
                    oldState.copy(isLoading = false)
                }

                onSuccess.invoke(result.data)
            }

            is Resource.Error -> {
                _state.update { oldState ->
                    oldState.copy(isLoading = false, errorMsg = Event(result.message))
                }
            }
        }
    }

    fun addUser(user: User) = viewModelScope.launch {
        if (user.id !in sharedShift.first().users) {
            addUserToSharedShift(userId = user.id, sharedShiftId = args.sharedShiftId)
        } else {
            _state.update { oldState ->
                val message = "User is already part of the shared shift"
                oldState.copy(errorMsg = Event(message))
            }
        }
    }

    fun deleteUser(userId: String) {
        deleteUserFromSharedShift(userId = userId, sharedShiftId = args.sharedShiftId)
    }

    fun setLoading(isLoading: Boolean) = viewModelScope.launch {
        _state.update { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }
}