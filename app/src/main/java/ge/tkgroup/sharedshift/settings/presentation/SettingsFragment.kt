package ge.tkgroup.sharedshift.settings.presentation

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.databinding.FragmentSettingsBinding

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val viewModel: SettingsViewModel by viewModels()

    override fun listeners() {
        binding.btnSignOut.setOnClickListener {
            viewModel.signOut()
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToAuthFragment())
        }

        binding.btnCompanyCenter.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMySharedShiftsFragment())
        }
    }
}