package ge.tkgroup.sharedshift.common.data.remote.model

import com.google.firebase.firestore.DocumentId

data class EmployeeDto(
    @DocumentId val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val personalNumber: String = "",
    val occupation: String = "",
    val iban: String = "",
    val salaryInGEL: Double = 0.0,
    val hasPensionTaxDeduction: Boolean = true
)