package ge.tkgroup.sharedshift.settings.domain.usecases

import androidx.paging.PagingSource
import com.google.firebase.firestore.QuerySnapshot
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.domain.repository.AuthRepository
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.utils.UserNotAuthenticatedException
import javax.inject.Inject

class FetchMySharedShifts @Inject constructor(
    private val authRepository: AuthRepository,
    private val sharedShiftRepository: SharedShiftRepository
) {
    operator fun invoke(): PagingSource<QuerySnapshot, SharedShift> {
        val currentUserId = authRepository.getCurrentUserId() ?: throw UserNotAuthenticatedException()

        return sharedShiftRepository.fetchMySharedShifts(currentUserId)
    }
}