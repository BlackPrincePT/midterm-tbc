package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.permissions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.model.Permission
import ge.tkgroup.sharedshift.common.utils.Resource
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchSharedShiftById
import ge.tkgroup.sharedshift.settings.domain.usecases.UpdateUserPermissions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserPermissionsViewModel @Inject constructor(
    private val fetchSharedShiftById: FetchSharedShiftById,
    val updateUserPermissions: UpdateUserPermissions,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val args = EditUserPermissionsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    private val _items = MutableStateFlow(uncheckedPermissions())
    val items
        get() = _items.asStateFlow()

    init {
        applyExistingPermissions()
    }

    fun onAdapterCallbacks(callback: EditUserPermissionsAdapter.Callback) {
        when (callback) {
            is EditUserPermissionsAdapter.Callback.OnStatusChanged -> viewModelScope.launch {
                val newItems = _items.value.map {
                    if (it.id == callback.id)
                        it.copy(status = callback.status)
                    else
                        it
                }

                _items.update { newItems }
            }
        }
    }

    fun getResults(): List<Permission> = items.value.filter { it.status }.map { it.permission }

    private fun applyExistingPermissions() = viewModelScope.launch {
        val result = fetchSharedShiftById(args.sharedShiftId)

        if (result is Resource.Success) {
            val existingPermissions = result.data.permissions[args.userId]

            if (existingPermissions != null) {
                _items.update { _ ->
                    Permission.entries.map {
                        EditUserPermissionsAdapter.Item(
                            permission = it,
                            status = it in existingPermissions
                        )
                    }
                }
            }
        }
    }

    private fun uncheckedPermissions(): List<EditUserPermissionsAdapter.Item> {
        return Permission.entries.map {
            EditUserPermissionsAdapter.Item(permission = it)
        }
    }
}