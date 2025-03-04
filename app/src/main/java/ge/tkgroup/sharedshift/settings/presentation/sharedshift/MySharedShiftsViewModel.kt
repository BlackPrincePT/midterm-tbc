package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.settings.domain.usecases.CacheActiveSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchMySharedShifts
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySharedShiftsViewModel @Inject constructor(
    private val cacheActiveSharedShift: CacheActiveSharedShift,
    fetchMySharedShifts: FetchMySharedShifts
) : ViewModel() {

    val mySharedShifts = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { fetchMySharedShifts() }
    )
        .flow
        .cachedIn(viewModelScope)

    fun activateSharedShift(id: String) = viewModelScope.launch {
        cacheActiveSharedShift(id)
    }
}