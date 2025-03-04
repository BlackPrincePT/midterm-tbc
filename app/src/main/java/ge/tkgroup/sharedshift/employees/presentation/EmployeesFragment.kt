package ge.tkgroup.sharedshift.employees.presentation

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.viewLifecycleScope
import ge.tkgroup.sharedshift.databinding.FragmentEmployeesBinding
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class EmployeesFragment :
    BaseFragment<FragmentEmployeesBinding>(FragmentEmployeesBinding::inflate) {

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
                        navigateToCreate()
                        true
                    }

                    else -> false
                }
            }
        )
    }

    private fun onAdapterCallbacks(callback: EmployeesAdapter.Callback) {
        when (callback) {
            is EmployeesAdapter.Callback.OnClicked -> navigateToDetails(callback.employeeId)
        }
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

    private fun subscribeToViewStateUpdates(adapter: EmployeesAdapter) = with(binding) {
        collectLatest(viewModel.paginatedEmployees) {
            adapter.submitData(it)
        }
        collectLatest(adapter.loadStateFlow) { loadState ->
            val isRefreshing = loadState.refresh is LoadState.Loading
            val isAppending = loadState.append is LoadState.Loading

            swipeRefreshLayout.isRefreshing = isRefreshing || isAppending
            tvEmptyStatus.isVisible = adapter.itemCount == 0
        }
        swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
    }

    // ========= Navigation ========= \\

    private fun navigateToCreate() {
        findNavController().navigate(EmployeesFragmentDirections.employeesToCreateEmployee())
    }

    private fun navigateToDetails(employeeId: String) = viewLifecycleScope {
        val activeSharedShiftId = viewModel.activeSharedShiftId.first()
        findNavController().navigate(
            EmployeesFragmentDirections.employeesToEmployeeDetails(
                employeeId = employeeId,
                sharedShiftId = activeSharedShiftId
            )
        )
    }
}