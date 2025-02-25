package ge.tkgroup.sharedshift.common.utils

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
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

fun View.showSnackBar(body: String) {
    Snackbar.make(this, body, Snackbar.LENGTH_LONG).show()
}