package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
) {

    fun getYearMonth(): YearMonth {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"))
    }

    fun dailyAmount(): Double {
        return amount.toDouble() / getYearMonth().lengthOfMonth()
    }

    fun lastDay(): LocalDate = getYearMonth().atEndOfMonth()
    fun firstDay(): LocalDate = getYearMonth().atDay(1)

    fun getOverlappingAmount(period: Period) = dailyAmount() * period.getOverlappingDays(getPeriod())

    private fun getPeriod() = Period(firstDay(), lastDay())
}

