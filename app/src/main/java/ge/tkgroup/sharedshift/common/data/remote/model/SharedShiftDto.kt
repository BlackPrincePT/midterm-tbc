package ge.tkgroup.sharedshift.common.data.remote.model

import com.google.firebase.firestore.DocumentId
import ge.tkgroup.sharedshift.common.domain.model.Permission

data class SharedShiftDto(
    @DocumentId val id: String? = null,
    val companies: List<String> = emptyList(),
    val users: List<String> = emptyList(),
    val permissions: Map<String, List<String>> = emptyMap()
)