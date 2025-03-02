package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collect
import ge.tkgroup.sharedshift.databinding.FragmentMySharedShiftsBinding

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

    // ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): MySharedShiftsAdapter {
        return MySharedShiftsAdapter(onClicked = this::navigateToDetails)
    }

    private fun setupRecyclerView(mySharedShiftsAdapter: MySharedShiftsAdapter) {
        binding.rvMyCompanies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mySharedShiftsAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: MySharedShiftsAdapter) {
        collect(viewModel.mySharedShifts) {
            adapter.submitList(it)
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