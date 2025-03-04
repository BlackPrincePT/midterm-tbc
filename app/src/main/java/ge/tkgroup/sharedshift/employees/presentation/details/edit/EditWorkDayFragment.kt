package ge.tkgroup.sharedshift.employees.presentation.details.edit

import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.common.utils.extensions.collect
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.showAlert
import ge.tkgroup.sharedshift.databinding.FragmentEditWorkDayBinding
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class EditWorkDayFragment :
    BaseBottomSheetDialogFragment<FragmentEditWorkDayBinding>(FragmentEditWorkDayBinding::inflate) {

    private val viewModel: EditWorkDayViewModel by viewModels()

    override fun setup() {
        setupUI()
        assignExistingWorkDay()
    }

    override fun listeners() = with(binding) {
        tvCreate.setOnClickListener {
            val workDay = viewModel.getResult(
                etHoursWorked.text.toString(),
                switchOperator.isChecked,
                etNotes.text.toString()
            )

            showAlert(
                title = getString(R.string.are_you_sure),
                positiveButtonTitle = getString(R.string.save),
                onPositive = {
                    viewModel.saveWorkDay(viewModel.args.sharedShiftId, workDay)
                    findNavController().navigateUp()
                }
            )
        }

        tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        switchOperator.setOnClickListener {
            rvEquipmentUsage.isVisible = switchOperator.isChecked
        }
    }

    private fun assignExistingWorkDay() = with(binding) {
        val workDay = viewModel.args.workDay

        etHoursWorked.setText(workDay.hoursWorked.toString())
        etNotes.setText(workDay.notes)
        switchOperator.isChecked = workDay.exclusiveShift.isNotEmpty()
        rvEquipmentUsage.isVisible = workDay.exclusiveShift.isNotEmpty()

        viewModel.assignExistingExclusiveShifts(workDay.exclusiveShift)
    }

    private fun onAdapterCallbacks(callback: EditWorkDayAdapter.Callback) {
        when (callback) {
            is EditWorkDayAdapter.Callback.OnAdd -> viewModel.addRow()

            is EditWorkDayAdapter.Callback.OnHoursChanged ->
                viewModel.onHoursChanged(callback.id, callback.hours)

            is EditWorkDayAdapter.Callback.OnCompanyChanged ->
                viewModel.onCompanyChanged(callback.id, callback.company)

            is EditWorkDayAdapter.Callback.SetupAdapter -> {
                val companiesFlow = viewModel.activeSharedShift.map { sharedShift ->
                    sharedShift.companies
                }

                collectLatest(companiesFlow) { companies ->
                    val adapter =
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            companies
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    callback.spinner.adapter = adapter

                    val owner =
                        viewModel.items.value.first { item -> item.id == callback.id }.company

                    val position = companies.indexOfFirst { it == owner }

                    callback.spinner.setSelection(position)
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

    private fun createAdapter(): EditWorkDayAdapter {
        return EditWorkDayAdapter(
            callback = this::onAdapterCallbacks
        )
    }

    private fun setupRecyclerView(editWorkDayAdapter: EditWorkDayAdapter) {
        binding.rvEquipmentUsage.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = editWorkDayAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: EditWorkDayAdapter) {
        collect(viewModel.items) {
            adapter.submitList(it)
        }
    }
}