package ge.tkgroup.sharedshift.common.domain.repository

import com.google.firebase.Timestamp
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import kotlinx.coroutines.flow.Flow

interface WorkDayRepository {
    fun createWorkDay(sharedShiftId: String, workDay: WorkDay)
    fun updateWorkDay(sharedShiftId: String, workDay: WorkDay)
    fun observeWorkDays(
        sharedShiftId: String, employeeId: String,
        startTimestamp: Timestamp, endTimestamp: Timestamp
    ): Flow<List<WorkDay>>
}