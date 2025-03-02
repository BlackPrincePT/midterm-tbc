package ge.tkgroup.sharedshift.employees.presentation

import ge.tkgroup.sharedshift.common.presentation.Event

data class EmployeesViewState(
    val isLoading: Boolean = false,
    val errorMsg: Event<String>? = null
)