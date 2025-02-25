package ge.tkgroup.sharedshift.settings.presentation.sharedshift

import androidx.navigation.fragment.findNavController
import ge.tkgroup.sharedshift.R
import ge.tkgroup.sharedshift.common.utils.BaseFragment
import ge.tkgroup.sharedshift.common.utils.addMenuProvider
import ge.tkgroup.sharedshift.databinding.FragmentMySharedShiftsBinding

class MySharedShiftsFragment :
    BaseFragment<FragmentMySharedShiftsBinding>(FragmentMySharedShiftsBinding::inflate) {

    override fun setup() {
        addMenuProvider(onCreateMenu = { menu, menuInflater ->
            menuInflater.inflate(R.menu.my_shared_shifts_action_bar_menu, menu)
        }, onMenuItemSelected = { menuItem ->
            when (menuItem.itemId) {
                R.id.add -> {
                    findNavController().navigate(MySharedShiftsFragmentDirections.actionMySharedShiftsFragmentToCreateSharedShiftFragment())
                    true
                }

                else -> false
            }
        })
    }

}