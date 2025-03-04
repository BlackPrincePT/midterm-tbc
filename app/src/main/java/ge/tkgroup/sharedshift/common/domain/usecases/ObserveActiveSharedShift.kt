package ge.tkgroup.sharedshift.common.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class ObserveActiveSharedShift @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    operator fun invoke() = sharedShiftRepository.observeCachedActiveSharedShift()
}