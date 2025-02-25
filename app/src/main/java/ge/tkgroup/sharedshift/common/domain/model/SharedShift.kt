package ge.tkgroup.sharedshift.common.domain.model

data class SharedShift(
    val id: String,
    val companies: List<String>,
    val permissions: Map<String, List<Permission>>
)