package ge.tkgroup.sharedshift.common.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.SharedShiftDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.SharedShift
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import javax.inject.Inject

private const val SHARED_SHIFTS = "shared_shifts"

class SharedShiftRepositoryImpl @Inject constructor(
    private val sharedShiftDtoMapper: SharedShiftDtoMapper
) : SharedShiftRepository {

    private val db = Firebase.firestore

    override fun saveSharedShift(sharedShift: SharedShift) {
        val sharedShiftDto = sharedShiftDtoMapper.mapFromDomain(sharedShift)
        db.collection(SHARED_SHIFTS).document(sharedShift.id).set(sharedShiftDto)
    }
}