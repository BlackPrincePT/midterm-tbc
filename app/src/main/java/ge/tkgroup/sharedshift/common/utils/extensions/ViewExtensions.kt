package ge.tkgroup.sharedshift.common.utils.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(body: String) {
    Snackbar.make(this, body, Snackbar.LENGTH_SHORT).show()
}