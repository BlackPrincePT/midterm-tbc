package ge.tkgroup.sharedshift.settings.presentation.sharedshift.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.auth.domain.usecases.GetCurrentUserId
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.domain.usecases.SaveSharedShift
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSharedShiftViewModel @Inject constructor(
    private val saveSharedShiftToDatabase: SaveSharedShift,
    private val getCurrentUserId: GetCurrentUserId
) : ViewModel() {

    private val _items = MutableStateFlow(listOf(CreateSharedShiftAdapter.Item()))
    val items: StateFlow<List<CreateSharedShiftAdapter.Item>>
        get() = _items

    fun onAdapterCallbacks(callback: CreateSharedShiftAdapter.Callback) {
        when (callback) {
            is CreateSharedShiftAdapter.Callback.OnAdd -> viewModelScope.launch {
                val newItems = _items.value + CreateSharedShiftAdapter.Item()

                _items.update { newItems }
            }

            is CreateSharedShiftAdapter.Callback.OnTextChanged -> viewModelScope.launch {
                val newItems = _items.value.map {
                    if (it.id == callback.id)
                        it.copy(text = callback.text)
                    else
                        it
                }

                _items.update { newItems }
            }
        }
    }

    fun saveSharedShit(companies: List<String>): Boolean = getCurrentUserId()?.let {
        val sharedShift = SharedShift(id = it, companies = companies, permissions = emptyMap())
        saveSharedShiftToDatabase(sharedShift)
        true
    } ?: false
}