package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collect
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.databinding.FragmentMySharedShiftsBinding
import ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.UsersAdapter

@AndroidEntryPoint
class MySharedShiftsFragment :
    BaseFragment<FragmentMySharedShiftsBinding>(FragmentMySharedShiftsBinding::inflate) {

    private val viewModel: MySharedShiftsViewModel by viewModels()

    override fun setup() {
        setupActionBarMenu()
        setupUI()
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

    private fun onAdapterCallbacks(callback: MySharedShiftsAdapter.Callback) {
        when (callback) {
            is MySharedShiftsAdapter.Callback.OnClicked -> navigateToDetails(callback.sharedShiftId)
            is MySharedShiftsAdapter.Callback.OnLongClicked -> {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Choose this shared shift?")
                    setMessage(callback.sharedShift.companies.toString())
                    setNegativeButton("Cancel", null)
                    setPositiveButton("Choose") { _, _ ->
                        viewModel.activateSharedShift(callback.sharedShift.id)
                    }
                    create()
                    show()
                }
            }
        }
    }

    // ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): MySharedShiftsAdapter {
        return MySharedShiftsAdapter(callback = this::onAdapterCallbacks)
    }

    private fun setupRecyclerView(mySharedShiftsAdapter: MySharedShiftsAdapter) {
        binding.rvMyCompanies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mySharedShiftsAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: MySharedShiftsAdapter) = with(binding) {
        collect(viewModel.mySharedShifts) {
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
    }

    // ========= Navigation ========= \\

    private fun navigateToCreate() {
        findNavController().navigate(MySharedShiftsFragmentDirections.mySharedShiftsToCreateSharedShift())
    }

    private fun navigateToDetails(sharedShiftId: String) {
        findNavController().navigate(
            MySharedShiftsFragmentDirections.mySharedShiftsToEditSharedShift(sharedShiftId = sharedShiftId)
        )
    }
}