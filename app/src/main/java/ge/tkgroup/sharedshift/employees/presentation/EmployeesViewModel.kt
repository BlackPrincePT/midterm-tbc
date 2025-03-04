package ge.tkgroup.sharedshift.employees.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.employees.domain.usecases.GetEmployeesPagingSource
import ge.tkgroup.sharedshift.common.domain.usecases.ObserveActiveSharedShift
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

@HiltViewModel
class EmployeesViewModel @Inject constructor(
    getEmployeesPagingSource: GetEmployeesPagingSource,
    observeActiveSharedShift: ObserveActiveSharedShift
) : ViewModel() {

    val activeSharedShiftId = observeActiveSharedShift()

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedEmployees = activeSharedShiftId
        .flatMapMerge { sharedShiftId ->
            Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { getEmployeesPagingSource(sharedShiftId) }
            )
                .flow
                .cachedIn(viewModelScope)
        }
}