package ge.tkgroup.sharedshift.common.data.repository

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.snapshots
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.EMPLOYEE_ID
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.SHARED_SHIFTS
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.TIMESHEETS
import ge.tkgroup.sharedshift.common.data.remote.FirebaseConstants.TIMESTAMP
import ge.tkgroup.sharedshift.common.data.remote.model.WorkDayDto
import ge.tkgroup.sharedshift.common.data.remote.model.mappers.WorkDayDtoMapper
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.domain.repository.WorkDayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class WorkDayRepositoryImpl @Inject constructor(
    private val workDayDtoMapper: WorkDayDtoMapper
) : WorkDayRepository {

    private val db = Firebase.firestore

    override fun createWorkDay(sharedShiftId: String, workDay: WorkDay) {
        val workDayDto = workDayDtoMapper.mapFromDomain(workDay)

        db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(TIMESHEETS)
            .add(workDayDto)
    }

    override fun updateWorkDay(sharedShiftId: String, workDay: WorkDay) {
        val workDayDto = workDayDtoMapper.mapFromDomain(workDay)

        db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(TIMESHEETS)
            .document(workDay.id)
            .set(workDayDto)
    }

    override fun observeWorkDays(
        sharedShiftId: String, employeeId: String,
        startTimestamp: Timestamp, endTimestamp: Timestamp
    ): Flow<List<WorkDay>> {
        return db.collection(SHARED_SHIFTS)
            .document(sharedShiftId)
            .collection(TIMESHEETS)
            .whereEqualTo(EMPLOYEE_ID, employeeId)
            .whereGreaterThanOrEqualTo(TIMESTAMP, startTimestamp)
            .whereLessThanOrEqualTo(TIMESTAMP, endTimestamp)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(WorkDayDto::class.java)
                    .map(workDayDtoMapper::mapToDomain)
            }
    }
}