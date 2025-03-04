package ge.tkgroup.sharedshift.employees.presentation.details.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.employees.domain.usecases.FetchEquipmentById
import ge.tkgroup.sharedshift.employees.domain.usecases.FindEquipment
import ge.tkgroup.sharedshift.employees.domain.usecases.SaveWorkDay
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchSharedShift
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditWorkDayViewModel @Inject constructor(
    val saveWorkDay: SaveWorkDay,
    fetchSharedShift: FetchSharedShift,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val args = EditWorkDayFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val activeSharedShift = fetchSharedShift(args.sharedShiftId)
        .filterNotNull()

    private val _items = MutableStateFlow(listOf(EditWorkDayAdapter.Item()))
    val items get() = _items.asStateFlow()

    fun addRow() = viewModelScope.launch {
        val newItems = _items.value + EditWorkDayAdapter.Item()

        _items.update { newItems }
    }

    fun onHoursChanged(id: UUID, hours: String) = viewModelScope.launch {
        val newItems = _items.value.map {
            if (it.id == id) it.copy(hours = hours) else it
        }

        _items.update { newItems }
    }

    fun onCompanyChanged(id: UUID, company: String) {
        val newItems = _items.value.map {
            if (it.id == id) it.copy(company = company) else it
        }

        _items.update { newItems }
    }

    fun assignExistingExclusiveShifts(exclusiveShifts: Map<String, Int>) = viewModelScope.launch {
        if (exclusiveShifts.isEmpty())
            return@launch

        val newItems = mutableListOf<EditWorkDayAdapter.Item>()

        exclusiveShifts.forEach { (company, hours) ->
            val newItem = EditWorkDayAdapter.Item(hours = hours.toString(), company = company)
            newItems.add(newItem)
        }

        _items.update { newItems }
    }

    fun getResult(hoursWorked: String, usedEquipment: Boolean, notes: String?): WorkDay {
        val filteredItems = items.value.filter {
            it.company.isNotEmpty() && it.hours.isNotEmpty()
        }

        val exclusiveShifts = filteredItems.associate {
            it.company to it.hours.toInt()
        }

        return args.workDay.copy(
            hoursWorked = hoursWorked.toInt(),
            exclusiveShift = if (usedEquipment) exclusiveShifts else mapOf(),
            notes = notes
        )
    }
}