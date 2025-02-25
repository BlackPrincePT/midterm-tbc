package ge.tkgroup.sharedshift.settings.presentation.sharedshift.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.utils.collect
import ge.tkgroup.sharedshift.databinding.FragmentCreateSharedShiftBinding

@AndroidEntryPoint
class CreateSharedShiftFragment : BottomSheetDialogFragment() {

    private val viewModel: CreateSharedShiftViewModel by viewModels()

    private var _binding: FragmentCreateSharedShiftBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSharedShiftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        listeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun listeners() {
        with(binding) {
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
                        if (viewModel.saveSharedShit(companies = companies))
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