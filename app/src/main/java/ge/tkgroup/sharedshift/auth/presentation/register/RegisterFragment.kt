package ge.tkgroup.sharedshift.auth.presentation.register

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.databinding.FragmentRegisterBinding

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel: RegisterViewModel by viewModels()

    override fun listeners() = with(binding) {
        btnSubmit.setOnClickListener {
            viewModel.uploadUserData(username = etUsername.text.toString())

            navigateToEmployees()
        }
    }

    private fun navigateToEmployees() {
        findNavController().navigate(RegisterFragmentDirections.registerToEmployees())
    }
}
