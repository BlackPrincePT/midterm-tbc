package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchMySharedShifts
import javax.inject.Inject

@HiltViewModel
class MySharedShiftsViewModel @Inject constructor(
    fetchMySharedShifts: FetchMySharedShifts
) : ViewModel() {

    val mySharedShifts = fetchMySharedShifts()
}