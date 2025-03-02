package ge.tkgroup.sharedshift.common.domain.model

import java.time.LocalDate

data class WorkDay(
    val id: String,
    val employeeId: String,
    val hoursWorked: Int,
    val equipmentUsage: MutableMap<String, Int>,
    val timestamp: LocalDate,
    val notes: String?
) {
    val hoursWorkedTotal: Int
        get() {
            return if (equipmentUsage.isNotEmpty())
                equipmentUsage.values.reduce(Int::plus) + hoursWorked
            else
                hoursWorked
        }
}