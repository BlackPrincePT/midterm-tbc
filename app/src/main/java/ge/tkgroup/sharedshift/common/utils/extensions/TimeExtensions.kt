package ge.tkgroup.sharedshift.common.utils.extensions

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

fun Timestamp.toLocalDate(): LocalDate =
    LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
        .toLocalDate()

fun LocalDate.toTimestamp(): Timestamp = Timestamp(
    LocalDateTime.of(this.year, this.month, this.dayOfMonth, 0, 0)
        .toInstant(ZoneOffset.UTC)
)

fun isLeapYear(year: Int): Boolean = (year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)