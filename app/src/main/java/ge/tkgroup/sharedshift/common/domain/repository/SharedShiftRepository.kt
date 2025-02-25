package ge.tkgroup.sharedshift.common.domain.repository

import ge.tkgroup.sharedshift.common.domain.model.SharedShift

interface SharedShiftRepository {
    fun saveSharedShift(sharedShift: SharedShift)
}