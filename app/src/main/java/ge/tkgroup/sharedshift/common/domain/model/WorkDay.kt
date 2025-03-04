package ge.tkgroup.sharedshift.common.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate

@Parcelize
data class WorkDay(
    val id: String,
    val employeeId: String,
    val hoursWorked: Int,
    val exclusiveShift: Map<String, Int>,
    val date: LocalDate,
    val notes: String?
): Parcelable {
    val hoursWorkedTotal: Int
        get() {
            return if (exclusiveShift.isNotEmpty())
                exclusiveShift.values.reduce(Int::plus) + hoursWorked
            else
                hoursWorked
        }

    val hoursWorkedString: String
        get() = when (hoursWorkedTotal) {
            8 -> "Full Day"
            4 -> "Half Day"
            0 -> "Absent"
            else -> "$hoursWorkedTotal Hours"
        }
}