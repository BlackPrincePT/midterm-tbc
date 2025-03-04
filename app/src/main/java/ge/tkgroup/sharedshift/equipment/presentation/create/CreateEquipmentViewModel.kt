package ge.tkgroup.sharedshift.equipment.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.equipment.model.usecases.CreateEquipment
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEquipmentViewModel @Inject constructor(
    private val createEquipment: CreateEquipment
) : ViewModel() {

    fun addEquipment(equipment: Equipment) = viewModelScope.launch {
        createEquipment(equipment)
    }
}