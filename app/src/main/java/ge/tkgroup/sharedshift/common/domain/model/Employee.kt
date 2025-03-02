package ge.tkgroup.sharedshift.common.domain.model

data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val personalNumber: String,
    val occupation: String,
    val iban: String,
    val salaryInGEL: Double,
    val hasPensionTaxDeduction: Boolean
) {
    val fullName: String
        get() = "$firstName $lastName"

    val fullInfo: String
        get() = """
            Name: $fullName
            Occupation: $occupation
            Personal Number: $personalNumber
            IBAN: $iban
            Salary: ${salaryInGEL}GEL
            Has Pension Tax Deduction: $hasPensionTaxDeduction
        """.trimIndent()
}