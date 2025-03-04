package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class ClearActiveSharedShift @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    suspend operator fun invoke() = sharedShiftRepository.clearCachedActiveShift()
}