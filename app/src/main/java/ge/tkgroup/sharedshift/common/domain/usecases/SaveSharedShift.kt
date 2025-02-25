package ge.tkgroup.sharedshift.common.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class SaveSharedShift @Inject constructor(
    private val sharedShiftRepository: SharedShiftRepository
) {
    operator fun invoke(sharedShift: SharedShift) {
        sharedShiftRepository.saveSharedShift(sharedShift)
    }
}