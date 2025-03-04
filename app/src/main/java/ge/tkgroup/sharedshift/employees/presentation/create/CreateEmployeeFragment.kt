package ge.tkgroup.sharedshift.employees.presentation.create

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.common.utils.extensions.EMPTY
import ge.tkgroup.sharedshift.common.utils.extensions.showAlert
import ge.tkgroup.sharedshift.databinding.FragmentCreateEmployeeBinding

@AndroidEntryPoint
class CreateEmployeeFragment :
    BaseBottomSheetDialogFragment<FragmentCreateEmployeeBinding>(FragmentCreateEmployeeBinding::inflate) {

    private val viewModel: CreateEmployeeViewModel by viewModels()

    override fun listeners() = with(binding) {
        tvCreate.setOnClickListener {
            val employee = createEmployee()

            showAlert(
                title = getString(R.string.create_a_new_employee),
                message = employee.fullInfo,
                positiveButtonTitle = getString(R.string.create),
                onPositive = { viewModel.addEmployee(employee); findNavController().navigateUp() })
        }

        tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun createEmployee(): Employee = with(binding) {
        return Employee(
            id = String.EMPTY,
            firstName = etFirstName.text.toString().trim(),
            lastName = etLastName.text.toString().trim(),
            personalNumber = etPersonalNumber.text.toString().trim(),
            occupation = etOccupation.text.toString().trim(),
            iban = etIBAN.text.toString().trim(),
            salaryInGEL = etSalary.text.toString().toDouble(),
            hasPensionTaxDeduction = switchPensionTax.isChecked
        )
    }
}