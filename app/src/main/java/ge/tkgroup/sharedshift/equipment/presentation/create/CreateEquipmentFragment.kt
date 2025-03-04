package ge.tkgroup.sharedshift.equipment.presentation.create

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ge.tkgroup.sharedshift.common.domain.model.Equipment
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.common.utils.extensions.EMPTY
import ge.tkgroup.sharedshift.databinding.FragmentCreateEquipmentBinding

@AndroidEntryPoint
class CreateEquipmentFragment :
    BaseBottomSheetDialogFragment<FragmentCreateEquipmentBinding>(FragmentCreateEquipmentBinding::inflate) {

    private val viewModel: CreateEquipmentViewModel by viewModels()

    override fun listeners() = with(binding) {
        tvCreate.setOnClickListener {
            val equipment = createEquipment()

            AlertDialog.Builder(requireContext()).apply {
                setTitle("Create a new Equipment")
                setMessage(equipment.toString())
                setNegativeButton("Cancel", null)
                setPositiveButton("Create") { _, _ ->
                    viewModel.addEquipment(equipment)
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

    private fun createEquipment(): Equipment = with(binding) {
        return Equipment(
            id = String.EMPTY,
            manufacturer = etManufacturer.text.toString(),
            model = etModel.text.toString(),
            licensePlate = etLicensePlate.text.toString(),
            owner = etOwner.text.toString()
        )
    }
}