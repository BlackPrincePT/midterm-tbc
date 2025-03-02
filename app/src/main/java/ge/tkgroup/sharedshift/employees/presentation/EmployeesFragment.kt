package ge.tkgroup.sharedshift.employees.presentation

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.showSnackBar
import ge.tkgroup.sharedshift.databinding.FragmentEmployeesBinding

@AndroidEntryPoint
class EmployeesFragment : BaseFragment<FragmentEmployeesBinding>(FragmentEmployeesBinding::inflate) {

    private val viewModel: EmployeesViewModel by viewModels()

    override fun setup() {
        setupUI()
        setupActionBarMenu()
    }

    private fun setupActionBarMenu() {
        addMenuProvider(
            onCreateMenu = { menu, menuInflater ->
                menuInflater.inflate(R.menu.add_only_menu, menu)
            }, onMenuItemSelected = { menuItem ->
                when (menuItem.itemId) {
                    R.id.add -> {

                        true
                    }

                    else -> false
                }
            }
        )
    }

    private fun onAdapterCallbacks(callback: EmployeesAdapter.Callback) {

    }

    // ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): EmployeesAdapter {
        return EmployeesAdapter(callback = this::onAdapterCallbacks)
    }

    private fun setupRecyclerView(employeesAdapter: EmployeesAdapter) {
        binding.rvEmployees.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = employeesAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: EmployeesAdapter) {
        collectLatest(viewModel.paginatedEmployees) {
            adapter.submitData(it)
        }
        collectLatest(adapter.loadStateFlow) { loadState ->
            viewModel.setLoading(loadState.append is LoadState.Loading)
        }
        collectLatest(viewModel.state) { state ->
            binding.progressBar.isVisible = state.isLoading

            state.errorMsg?.getContentIfNotHandled()?.let {
                binding.root.showSnackBar(it)
            }
        }
    }
}