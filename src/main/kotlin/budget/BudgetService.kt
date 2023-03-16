package budget

import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (startDate.isAfter(endDate)) return 0.0
        val budgets = budgetRepo.getAll()
        val startYearMonth = YearMonth.from(startDate)
        var currentYearMonth = startYearMonth
        val endYearMonth = YearMonth.from(endDate)
        var amount = 0.00
        while (currentYearMonth <= endYearMonth) {
            budgets.find { it.getYearMonth() == currentYearMonth }?.let { budget ->
                val overlappingStart = if (budget.getYearMonth() == startYearMonth) startDate else budget.firstDay()
                val overlappingEnd = if (budget.getYearMonth() == endYearMonth) endDate else budget.lastDay()
                val dailyAmount = budget.dailyAmount()
                val overlappingDays = ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1
                amount += dailyAmount * overlappingDays
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }
}
