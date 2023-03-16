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
                if (startYearMonth == endYearMonth) {
                    return budget.dailyAmount() * (ChronoUnit.DAYS.between(startDate, endDate) + 1)
                }
                val overlappingStart: LocalDate
                val overlappingEnd: LocalDate
                val dailyAmount = budget.dailyAmount()
                if (budget.getYearMonth() == startYearMonth) {
                    overlappingStart = startDate
                    overlappingEnd = budget.lastDay()
                } else if (budget.getYearMonth() == endYearMonth) {
                    overlappingStart = budget.firstDay()
                    overlappingEnd = endDate
                } else {
                    overlappingStart = budget.firstDay()
                    overlappingEnd = budget.lastDay()
                }
                amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, overlappingEnd) + 1)
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }
}
