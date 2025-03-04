package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.domain.repository.SharedShiftRepository
import ge.tkgroup.sharedshift.common.domain.repository.WorkDayRepository
import ge.tkgroup.sharedshift.common.utils.extensions.toTimestamp
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class ObserveWorkDays @Inject constructor(private val workDayRepository: WorkDayRepository) {
    operator fun invoke(
        activeSharedShiftId: String,
        employeeId: String,
        date: LocalDate
    ): Flow<List<WorkDay>> {
        val startDate = LocalDate.of(date.year, date.month, 1)
        val endDate = LocalDate.of(date.year, date.month, date.lengthOfMonth())

        return workDayRepository.observeWorkDays(
            sharedShiftId = activeSharedShiftId,
            employeeId = employeeId,
            startTimestamp = startDate.toTimestamp(),
            endTimestamp = endDate.toTimestamp()
        )
    }
}