package ge.tkgroup.sharedshift.common.domain.model

data class Equipment(
    val id: String,
    val manufacturer: String,
    val model: String,
    val category: String,
    val licensePlate: String,
    val ownerId: String
) {
    val info: String
        get() = "$manufacturer $model ($licensePlate)"
}