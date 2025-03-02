package ge.tkgroup.sharedshift.common.data.remote.model

import com.google.firebase.firestore.DocumentId

data class EquipmentDto(
    @DocumentId val id: String? = null,
    val manufacturer: String = "",
    val model: String = "",
    val category: String = "",
    val licensePlate: String = "",
    val ownerId: String = ""
)