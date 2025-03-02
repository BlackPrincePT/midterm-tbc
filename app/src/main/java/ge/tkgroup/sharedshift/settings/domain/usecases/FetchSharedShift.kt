package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class FetchSharedShift @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    operator fun invoke(id: String) = sharedShiftRepository.fetchSharedShift(id)
}