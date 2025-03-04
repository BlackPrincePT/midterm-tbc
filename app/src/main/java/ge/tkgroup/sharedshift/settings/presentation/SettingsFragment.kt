package ge.tkgroup.sharedshift.settings.presentation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun setup() {
        subscribeToViewStateUpdates()
    }

    override fun listeners() = with(binding) {
        btnSignOut.setOnClickListener {
            viewModel.signOut()
        }

        btnCompanyCenter.setOnClickListener {
            navigateToSharedShifts()
        }
    }

    private fun subscribeToViewStateUpdates() {
        collectLatest(viewModel.state) { state ->
            if (state.shouldSignOut)
                navigateToAuth()
        }
        collectLatest(viewModel.currentUser) { currentUser ->
            binding.tvUsername.text = currentUser.username
        }
    }

    // ========= Navigation ========= \\

    private fun navigateToAuth() {
        findNavController().navigate(SettingsFragmentDirections.settingsToAuth())
    }

    private fun navigateToSharedShifts() {
        findNavController().navigate(SettingsFragmentDirections.settingsToMySharedShifts())
    }
}