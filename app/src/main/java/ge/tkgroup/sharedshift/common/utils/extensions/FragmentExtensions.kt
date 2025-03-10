package ge.tkgroup.sharedshift.common.utils.extensions

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun Fragment.viewLifecycleScope(block: suspend CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            block.invoke(this)
        }
    }
}

fun <T> Fragment.collectLatest(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    viewLifecycleScope {
        flow.collectLatest {
            action.invoke(it)
        }
    }
}

fun <T> Fragment.collect(flow: Flow<T>, action: suspend (value: T) -> Unit) {
    viewLifecycleScope {
        flow.collect {
            action.invoke(it)
        }
    }
}

fun Fragment.addMenuProvider(
    onCreateMenu: (Menu, MenuInflater) -> Unit,
    onMenuItemSelected: (MenuItem) -> Boolean
) {
    requireActivity().addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            onCreateMenu.invoke(menu, menuInflater)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return onMenuItemSelected.invoke(menuItem)
        }

    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}

fun Fragment.showDatePickerDialog(onDateSet: (DatePicker, Int, Int, Int) -> Unit) {
    Calendar.getInstance().let { calendar ->
        DatePickerDialog(
            requireContext(),
            onDateSet,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

fun Fragment.showAlert(
    title: String, message: String? = null, positiveButtonTitle: String,
    onPositive: () -> Unit
) {
    AlertDialog.Builder(requireContext()).apply {
        setTitle(title)
        message?.let {
            setMessage(it)
        }
        setNegativeButton("Cancel", null)
        setPositiveButton(positiveButtonTitle) { _, _ ->
            onPositive.invoke()
        }
        create()
        show()
    }
}