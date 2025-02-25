package ge.tkgroup.sharedshift.common.data.remote.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import ge.tkgroup.sharedshift.common.domain.model.Employee
import ge.tkgroup.sharedshift.common.utils.MappingException
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeDto(
    @DocumentId val id: String? = null,
    val firstName: String = "",
    val lastName: String = "",
    val personalNumber: String = "",
    val occupation: String = "",
    val iban: String = "",
    val salaryInGEL: Double = 0.0,
    val hasPensionTaxDeduction: Boolean = true
) : Parcelable {

    fun toDomain(): Employee {
        return Employee(
            id = id ?: throw MappingException("Employee ID cannot be null"),
            firstName = firstName,
            lastName = lastName,
            personalNumber = personalNumber,
            occupation = occupation,
            iban = iban,
            salaryInGEL = salaryInGEL,
            hasPensionTaxDeduction = hasPensionTaxDeduction
        )
    }
}