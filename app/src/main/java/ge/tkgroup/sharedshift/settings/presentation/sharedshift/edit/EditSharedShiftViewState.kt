package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit

import ge.tkgroup.sharedshift.common.presentation.Event

data class EditSharedShiftViewState(
    val isLoading: Boolean = false,
    val errorMsg: Event<String>? = null
)