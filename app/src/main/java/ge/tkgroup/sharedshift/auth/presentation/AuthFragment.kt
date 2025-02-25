package ge.tkgroup.sharedshift.auth.presentation

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.collectLatest
import ge.tkgroup.sharedshift.common.utils.showSnackBar
import ge.tkgroup.sharedshift.common.utils.viewLifecycleScope
import ge.tkgroup.sharedshift.databinding.FragmentAuthBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {

    private val viewModel: AuthViewModel by viewModels()

    override fun setup() {
        if (viewModel.hasSavedAuthSession())
            onSuccess()
    }

    override fun listeners() {
        binding.btnGoogle.setOnClickListener {
            viewModel.button { onSuccess() }
        }
    }

    private fun onSuccess() {
        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToEmployeesFragment())
    }

    override fun collectors() {
        collectLatest(viewModel.state) {
            binding.root.showSnackBar(it)
        }
    }
}