package budget

import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Period(val startDate: LocalDate, val endDate: LocalDate) {
    fun getOverlappingDays(otherPeriod: Period): Long {
        val overlappingStart = if (otherPeriod.startDate < startDate) startDate else otherPeriod.startDate
        val overlappingEnd = if (otherPeriod.endDate > endDate) endDate else otherPeriod.endDate
        val days = ChronoUnit.DAYS.between(overlappingStart, overlappingEnd)
        return if (days >= 0) days + 1 else 0
    }
}
