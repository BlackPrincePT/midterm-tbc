package ge.tkgroup.sharedshift.auth.presentation.auth

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.showSnackBar
import ge.tkgroup.sharedshift.databinding.FragmentAuthBinding

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    private val viewModel: AuthViewModel by viewModels()

    override fun setup() {
        subscribeToViewStateUpdates()
    }

    override fun listeners() {
        binding.btnGoogle.setOnClickListener {
            viewModel.authenticate(requireContext())
        }
    }

    private fun subscribeToViewStateUpdates() {
        collectLatest(viewModel.state) {
            updateScreenState(it)
        }
    }

    private fun updateScreenState(state: AuthViewState) {
        binding.progressBar.isVisible = state.isLoading

        if (state.isAuthenticated) {
            onAuthSuccess(state.isRegistrationComplete)
        }

        state.errorMsg?.getContentIfNotHandled()?.let {
            binding.root.showSnackBar(it)
        }
    }

    private fun onAuthSuccess(isRegistrationComplete: Boolean) = with(AuthFragmentDirections) {
        findNavController().navigate(
            if (isRegistrationComplete) authToEmployees() else authToRegister()
        )
    }
}