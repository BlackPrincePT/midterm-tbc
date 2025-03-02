package ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.find

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import ge.tkgroup.sharedshift.common.utils.BaseBottomSheetDialogFragment
import ge.tkgroup.sharedshift.databinding.FragmentFindUserByUsernameBinding
import ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.EditSharedShiftFragment.Companion.USERNAME_REQUEST_KEY
import ge.tkgroup.sharedshift.settings.presentation.sharedshift.edit.EditSharedShiftFragment.Companion.USERNAME_RESULT_KEY

class FindUserByUsernameFragment : BaseBottomSheetDialogFragment<FragmentFindUserByUsernameBinding>(
    FragmentFindUserByUsernameBinding::inflate
) {
    override fun listeners() = with(binding) {
        tvFind.setOnClickListener {
            val username = etUsername.text.toString()
            val bundle = bundleOf(USERNAME_RESULT_KEY to username)

            setFragmentResult(USERNAME_REQUEST_KEY, bundle)
            findNavController().navigateUp()
        }

        tvCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}