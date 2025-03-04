package ge.tkgroup.sharedshift.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class SharedShift(
    val id: String,
    val companies: List<String>,
    val users: List<String>,
    val permissions: Map<String, List<Permission>>,
    val isActive: Boolean = false
)