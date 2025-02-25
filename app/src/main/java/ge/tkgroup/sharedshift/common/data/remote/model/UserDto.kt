package ge.tkgroup.sharedshift.common.data.remote.model

import com.google.firebase.firestore.DocumentId

data class UserDto(
    @DocumentId val id: String? = null,
    val username: String = ""
)