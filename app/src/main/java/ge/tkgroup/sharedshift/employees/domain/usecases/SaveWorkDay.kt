package ge.tkgroup.sharedshift.employees.domain.usecases

import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.domain.repository.WorkDayRepository
import javax.inject.Inject

class SaveWorkDay @Inject constructor(private val workDayRepository: WorkDayRepository) {
    operator fun invoke(activeSharedShiftId: String, workDay: WorkDay) {
        if (workDay.id.isEmpty())
            workDayRepository.createWorkDay(activeSharedShiftId, workDay)
        else
            workDayRepository.updateWorkDay(activeSharedShiftId, workDay)
    }
}