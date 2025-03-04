package ge.tkgroup.sharedshift.common.domain.model

data class Equipment(
    val id: String,
    val manufacturer: String,
    val model: String,
    val licensePlate: String,
    val owner: String
) {
    private val info: String
        get() = "$manufacturer $model - $owner ($licensePlate)"

    override fun toString(): String = info
}