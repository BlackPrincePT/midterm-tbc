package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.permissions

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.databinding.FragmentEditUserPermissionsBinding

@AndroidEntryPoint
class EditUserPermissionsFragment :
    BaseBottomSheetDialogFragment<FragmentEditUserPermissionsBinding>(
        FragmentEditUserPermissionsBinding::inflate
    ) {

    private val viewModel: EditUserPermissionsViewModel by viewModels()

    override fun setup() {
        setupUI()
    }

    override fun listeners() = with(binding) {
        tvSave.setOnClickListener {
            val permissions = viewModel.getResults()

            AlertDialog.Builder(requireContext()).apply {
                setTitle("Create a new SharedShift")
                setMessage(permissions.joinToString(separator = ",\n"))
                setNegativeButton("Cancel", null)
                setPositiveButton("Create") { _, _ ->
                    viewModel.updateUserPermissions(
                        sharedShiftId = viewModel.args.sharedShiftId,
                        userid = viewModel.args.userId,
                        permissions
                    )
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

    private fun createAdapter(): EditUserPermissionsAdapter {
        return EditUserPermissionsAdapter(callbacks = viewModel::onAdapterCallbacks)
    }

    private fun setupRecyclerView(editUserPermissionsAdapter: EditUserPermissionsAdapter) {
        binding.rvPermissions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = editUserPermissionsAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: EditUserPermissionsAdapter) {
        collectLatest(viewModel.items) {
            adapter.submitList(it)
        }
    }
}