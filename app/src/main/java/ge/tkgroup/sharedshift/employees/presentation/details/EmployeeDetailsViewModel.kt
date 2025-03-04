package ge.tkgroup.sharedshift.employees.presentation.details

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.tkgroup.sharedshift.common.domain.model.WorkDay
import ge.tkgroup.sharedshift.common.domain.usecases.FetchUserById
import ge.tkgroup.sharedshift.common.utils.Resource
import ge.tkgroup.sharedshift.common.utils.extensions.EMPTY
import ge.tkgroup.sharedshift.employees.domain.usecases.FetchEmployeeById
import ge.tkgroup.sharedshift.employees.domain.usecases.ObserveWorkDays
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchSharedShift
import ge.tkgroup.sharedshift.settings.domain.usecases.FetchSharedShiftById
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EmployeeDetailsViewModel @Inject constructor(
    private val fetchEmployeeById: FetchEmployeeById,
    observeSharedShift: FetchSharedShift,
    observeWorkDays: ObserveWorkDays,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val args = EmployeeDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)

    val selectedDate = MutableStateFlow(LocalDate.now())

    private val sharedShift = observeSharedShift(args.sharedShiftId)
        .filterNotNull()

    private val templateWorkDays = selectedDate
        .map { date ->
            (1..date.lengthOfMonth()).map { day ->
                WorkDay(
                    id = String.EMPTY,
                    employeeId = args.employeeId,
                    hoursWorked = 0,
                    exclusiveShift = emptyMap(),
                    date = LocalDate.of(date.year, date.month, day),
                    notes = null
                )
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val fetchedWorkDays = selectedDate
        .flatMapMerge { date ->
            observeWorkDays(
                activeSharedShiftId = args.sharedShiftId,
                employeeId = args.employeeId,
                date = date
            )
        }

    val workDays = combine(templateWorkDays, fetchedWorkDays) { templateList, fetchedList ->
        templateList.map { template ->
            fetchedList.firstOrNull { it.date.dayOfMonth == template.date.dayOfMonth } ?: template
        }
    }

    suspend fun calculateSalary(): Map<String, String> = withContext(Dispatchers.Default) {
        when (val employeeResource = fetchEmployeeById(args.employeeId)) {
            is Resource.Success -> {

                val exclusiveHours: Map<String, Int> = workDays.first()
                    .map { it.exclusiveShift }
                    .flatMap { it.entries }
                    .groupBy({ it.key }, { it.value })
                    .mapValues { (_, values) -> values.sum() }

                val reserveHours = workDays.first().sumOf { it.hoursWorked }

                val fullSalary = employeeResource.data.salaryInGEL
                val pensionDeductedSalary = if (employeeResource.data.hasPensionTaxDeduction) {
                    fullSalary * 0.98
                } else {
                    fullSalary
                }

                val totalExclusiveHours = exclusiveHours.values.sum()
                val totalHours = totalExclusiveHours + reserveHours

                val standardWorkHours = 25.0 * 8.0
                val adjustedSalary =
                    pensionDeductedSalary * totalHours.toDouble() / standardWorkHours

                val initialSalaryDistribution: Map<String, Double> =
                    exclusiveHours.mapValues { (_, hours) ->
                        hours.toDouble() / totalHours.toDouble() * adjustedSalary
                    }

                val companies = sharedShift.first().companies
                val reserveSalaryPerCompany =
                    reserveHours.toDouble() / totalHours.toDouble() * adjustedSalary / companies.size
                val reserveSalaryDistribution = companies.associateWith { reserveSalaryPerCompany }

                (initialSalaryDistribution.entries + reserveSalaryDistribution.entries)
                    .groupBy({ it.key }, { it.value })
                    .mapValues { (_, salaries) ->
                        String.format(
                            Locale.getDefault(),
                            format = "%.2f",
                            salaries.sum()
                        )
                    }
            }

            is Resource.Error -> emptyMap()
        }
    }


    fun changeDate(newDate: LocalDate) {
        selectedDate.update { newDate }
    }
}