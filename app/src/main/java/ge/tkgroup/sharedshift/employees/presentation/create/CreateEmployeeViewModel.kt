package ge.tkgroup.sharedshift.employees.presentation.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.employees.domain.usecases.CreateEmployee
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEmployeeViewModel @Inject constructor(
    private val createEmployee: CreateEmployee
): ViewModel() {

    fun addEmployee(employee: Employee) = viewModelScope.launch {
        createEmployee(employee)
    }
}