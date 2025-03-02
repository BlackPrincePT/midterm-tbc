package ge.tkgroup.sharedshift.settings.presentation.sharedshift.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.settings.domain.usecases.CreateSharedShift
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateSharedShiftViewModel @Inject constructor(
    val createSharedShit: CreateSharedShift
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
}