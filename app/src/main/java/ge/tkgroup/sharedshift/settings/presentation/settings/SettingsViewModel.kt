package ge.tkgroup.sharedshift.settings.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.settings.domain.usecases.SignUserOut
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val signUserOut: SignUserOut
) : ViewModel() {

}