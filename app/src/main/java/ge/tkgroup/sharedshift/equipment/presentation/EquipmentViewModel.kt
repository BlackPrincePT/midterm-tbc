package ge.tkgroup.sharedshift.equipment.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.usecases.ObserveActiveSharedShift
import ge.tkgroup.sharedshift.equipment.model.usecases.GetEquipmentPagingSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

@HiltViewModel
class EquipmentViewModel @Inject constructor(
    observeActiveSharedShift: ObserveActiveSharedShift,
    getEquipmentPagingSource: GetEquipmentPagingSource
): ViewModel() {

    private val activeSharedShiftId = observeActiveSharedShift()

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedEquipment = activeSharedShiftId
        .flatMapMerge { sharedShiftId ->
            Pager(
                config = PagingConfig(pageSize = 10),
                pagingSourceFactory = { getEquipmentPagingSource(sharedShiftId) }
            )
                .flow
                .cachedIn(viewModelScope)
        }
}