package ge.tkgroup.sharedshift.auth.presentation.auth

import ge.tkgroup.sharedshift.common.presentation.Event

data class AuthViewState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isRegistrationComplete: Boolean = true,
    val errorMsg: Event<String>? = null
)