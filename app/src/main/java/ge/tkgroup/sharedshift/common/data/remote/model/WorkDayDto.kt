package ge.tkgroup.sharedshift.common.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class WorkDayDto(
    @DocumentId val id: String? = null,
    val employeeId: String = "",
    val hoursWorked: Int = 0,
    val equipmentUsage: Map<String, Int> = emptyMap(),
    val timestamp: Timestamp = Timestamp.now(),
    val notes: String? = null
)