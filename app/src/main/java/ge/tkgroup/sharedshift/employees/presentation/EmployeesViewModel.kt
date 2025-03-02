package ge.tkgroup.sharedshift.employees.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.employees.domain.usecases.GetEmployeesPagingSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    private val getEmployeesPagingSource: GetEmployeesPagingSource
) : ViewModel() {

    private val _state = MutableStateFlow(EmployeesViewState())
    val state = _state.asStateFlow()

    val paginatedEmployees = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { getEmployeesPagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)


    fun setLoading(isLoading: Boolean) = viewModelScope.launch {
        _state.update { oldState ->
            oldState.copy(isLoading = isLoading)
        }
    }
}