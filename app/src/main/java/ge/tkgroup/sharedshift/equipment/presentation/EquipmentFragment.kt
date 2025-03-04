package ge.tkgroup.sharedshift.equipment.presentation

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
import ge.tkgroup.sharedshift.databinding.FragmentEquipmentBinding

@AndroidEntryPoint
class EquipmentFragment :
    BaseFragment<FragmentEquipmentBinding>(FragmentEquipmentBinding::inflate) {

    private val viewModel: EquipmentViewModel by viewModels()

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
    // ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): EquipmentAdapter {
        return EquipmentAdapter()
    }

    private fun setupRecyclerView(equipmentAdapter: EquipmentAdapter) {
        binding.rvEquipment.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = equipmentAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: EquipmentAdapter) = with(binding) {
        collectLatest(viewModel.paginatedEquipment) {
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
        findNavController().navigate(EquipmentFragmentDirections.equipmentToCreateEquipment())
    }
}