package ge.tkgroup.sharedshift.employees.presentation.details

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.extensions.addMenuProvider
import ge.tkgroup.sharedshift.common.utils.extensions.collectLatest
import ge.tkgroup.sharedshift.common.utils.extensions.showAlert
import ge.tkgroup.sharedshift.common.utils.extensions.showDatePickerDialog
import ge.tkgroup.sharedshift.common.utils.extensions.viewLifecycleScope
import ge.tkgroup.sharedshift.databinding.FragmentEmployeeDetailsBinding
import java.time.LocalDate

@AndroidEntryPoint
class EmployeeDetailsFragment :
    BaseFragment<FragmentEmployeeDetailsBinding>(FragmentEmployeeDetailsBinding::inflate) {

    private val viewModel: EmployeeDetailsViewModel by viewModels()

    override fun setup() {
        setupUI()
        setupActionBarMenu()
    }

    private fun onAdapterCallbacks(callback: WorkDaysAdapter.Callback) {
        when (callback) {
            is WorkDaysAdapter.Callback.OnClicked -> navigateToEdit(callback.workDay)
        }
    }

    private fun setupActionBarMenu() {
        addMenuProvider(
            onCreateMenu = { menu, menuInflater ->
                menuInflater.inflate(R.menu.action_bar_employee_details_menu, menu)
            }, onMenuItemSelected = { menuItem ->
                when (menuItem.itemId) {
                    R.id.info -> {
                        viewLifecycleScope {
                            showAlert(
                                title = viewModel.selectedDate.value.month.name,
                                message = "${viewModel.calculateSalary()}",
                                positiveButtonTitle = getString(R.string.ok),
                                onPositive = {}
                            )
                        }
                        true
                    }

                    R.id.calendar -> {
                        showDatePickerDialog { _, selectedYear, selectedMonth, selectedDay ->
                            LocalDate.of(selectedYear, (selectedMonth + 1), selectedDay).let {
                                viewModel.changeDate(newDate = it)
                            }
                        }
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

    private fun createAdapter(): WorkDaysAdapter {
        return WorkDaysAdapter(callback = this::onAdapterCallbacks)
    }

    private fun setupRecyclerView(workDaysAdapter: WorkDaysAdapter) {
        binding.rvWorkDays.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = workDaysAdapter
        }
    }

    private fun subscribeToViewStateUpdates(adapter: WorkDaysAdapter) {
        collectLatest(viewModel.workDays) {
            adapter.submitList(it)
        }
    }

    // ========= Navigation ========= \\

    private fun navigateToEdit(workDay: WorkDay) {
        findNavController().navigate(
            EmployeeDetailsFragmentDirections.employeeDetailsToEditWorkDay(
                workDay,
                viewModel.args.sharedShiftId
            )
        )
    }
}