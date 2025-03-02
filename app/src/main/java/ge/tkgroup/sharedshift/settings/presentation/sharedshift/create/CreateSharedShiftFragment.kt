package ge.tkgroup.sharedshift.settings.presentation.sharedshift.create

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.common.utils.extensions.collect
import ge.tkgroup.sharedshift.databinding.FragmentCreateSharedShiftBinding

@AndroidEntryPoint
class CreateSharedShiftFragment :
    BaseBottomSheetDialogFragment<FragmentCreateSharedShiftBinding>(FragmentCreateSharedShiftBinding::inflate) {

    private val viewModel: CreateSharedShiftViewModel by viewModels()

    override fun setup() {
        setupUI()
    }

    override fun listeners() = with(binding) {
        tvCreate.setOnClickListener {
            val companies = viewModel.items.value
                .map { it.text }
                .filter { it.isNotEmpty() }

            if (companies.isEmpty())
                return@setOnClickListener

            AlertDialog.Builder(requireContext()).apply {
                setTitle("Create a new SharedShift")
                setMessage(companies.joinToString(separator = ",\n"))
                setNegativeButton("Cancel", null)
                setPositiveButton("Create") { _, _ ->
                    viewModel.createSharedShit(companies = companies)
                    findNavController().navigateUp()
                }
                create()
                show()
            }
        }

        tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

// ========= Adapter ========= \\

    private fun setupUI() {
        val adapter = createAdapter()
        setupRecyclerView(adapter)
        subscribeToViewStateUpdates(adapter)
    }

    private fun createAdapter(): CreateSharedShiftAdapter {
        return CreateSharedShiftAdapter(callback = viewModel::onAdapterCallbacks)
    }

    private fun setupRecyclerView(createSharedShiftAdapter: CreateSharedShiftAdapter) {
        binding.rvCompanies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = createSharedShiftAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: CreateSharedShiftAdapter) {
        collect(viewModel.items) {
            adapter.submitList(it)
        }
    }
}