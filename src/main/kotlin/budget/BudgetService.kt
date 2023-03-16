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
                if (budget.getYearMonth() == startYearMonth) {
                    val overlappingEnd = budget.lastDay()
                    val dailyAmount = budget.dailyAmount()
                    amount += dailyAmount * (ChronoUnit.DAYS.between(startDate, overlappingEnd) + 1)
                } else if (budget.getYearMonth() == endYearMonth) {
                    val overlappingStart = budget.firstDay()
                    val dailyAmount = budget.dailyAmount()
                    amount += dailyAmount * (ChronoUnit.DAYS.between(overlappingStart, endDate) + 1)
                } else {
                    amount += budget.amount
                }
            }
            currentYearMonth = currentYearMonth.plusMonths(1)
        }
        return amount
    }
}
