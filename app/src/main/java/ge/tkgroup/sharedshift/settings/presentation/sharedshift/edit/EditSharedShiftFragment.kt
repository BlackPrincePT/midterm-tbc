package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit

import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.showAlert
import ge.tkgroup.sharedshift.common.utils.extensions.showSnackBar
import ge.tkgroup.sharedshift.databinding.FragmentEditSharedShiftBinding

@AndroidEntryPoint
class EditSharedShiftFragment :
    BaseFragment<FragmentEditSharedShiftBinding>(FragmentEditSharedShiftBinding::inflate) {

    private val viewModel: EditSharedShiftViewModel by viewModels()

    override fun setup() {
        setupUI()
        setupActionBarMenu()

        setFragmentResultListener(USERNAME_REQUEST_KEY) { _, bundle ->
            val username = bundle.getString(USERNAME_RESULT_KEY) ?: return@setFragmentResultListener

            viewModel.findUserByUsername(username) { user ->
                showAlert(
                    title = getString(R.string.add_user),
                    message = user.username,
                    positiveButtonTitle = getString(R.string.add),
                    onPositive = { viewModel.addUser(user) }
                )
            }
        }
    }

    private fun navigateToDetails(userId: String) {
        findNavController().navigate(
            EditSharedShiftFragmentDirections.editSharedShiftToEditUserPermissions(
                sharedShiftId = viewModel.args.sharedShiftId,
                userId = userId
            )
        )
    }

    private fun setupActionBarMenu() {
        addMenuProvider(
            onCreateMenu = { menu, menuInflater ->
                menuInflater.inflate(R.menu.add_only_menu, menu)
            }, onMenuItemSelected = { menuItem ->
                when (menuItem.itemId) {
                    R.id.add -> {
                        navigateToAddUserToSharedShift()
                        true
                    }

                    else -> false
                }
            }
        )
    }

    private fun navigateToAddUserToSharedShift() {
        findNavController().navigate(EditSharedShiftFragmentDirections.editSharedShiftToAddUserToSharedShift())
    }

    private fun onAdapterCallbacks(callback: UsersAdapter.Callback) {
        when (callback) {
            is UsersAdapter.Callback.OnClicked -> navigateToDetails(callback.userId)
            is UsersAdapter.Callback.OnLongClicked -> {
                showAlert(
                    title = getString(R.string.delete_user),
                    positiveButtonTitle = getString(R.string.delete),
                    onPositive = { viewModel.deleteUser(callback.userId) }
                )
            }
        }
    }

    // ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): UsersAdapter {
        return UsersAdapter(callback = this::onAdapterCallbacks)
    }

    private fun setupRecyclerView(usersAdapter: UsersAdapter) {
        binding.rvUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = usersAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: UsersAdapter) = with(binding) {
        collectLatest(viewModel.paginatedUsers) {
            adapter.submitData(it)
        }

        collectLatest(adapter.loadStateFlow) { loadState ->
            val isRefreshing = loadState.refresh is LoadState.Loading
            val isAppending = loadState.append is LoadState.Loading

            swipeRefreshLayout.isRefreshing = isRefreshing || isAppending
        }

        swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

        collectLatest(viewModel.state) { state ->
            state.errorMsg?.getContentIfNotHandled()?.let {
                root.showSnackBar(it)
            }
        }
    }

    companion object {
        const val USERNAME_REQUEST_KEY = "username_request"
        const val USERNAME_RESULT_KEY = "username"
    }
}