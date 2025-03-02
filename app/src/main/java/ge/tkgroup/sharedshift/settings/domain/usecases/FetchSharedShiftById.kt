package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class FetchSharedShiftById @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    suspend operator fun invoke(id: String) = sharedShiftRepository.fetchSharedShiftById(id)
}