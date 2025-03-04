package ge.tkgroup.sharedshift.settings.domain.usecases

import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

class CacheActiveSharedShift @Inject constructor(private val sharedShiftRepository: SharedShiftRepository) {
    suspend operator fun invoke(id: String) = sharedShiftRepository.cacheActiveSharedShift(id)
}