package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        var startYearMonth = YearMonth.from(startDate)
        val endYearMonth = YearMonth.from(endDate)
        val budgets = budgetRepo.getAll().filter {
            (it.getYearMonth() >= startYearMonth) && (it.getYearMonth() <= endYearMonth)
        }
        val yearMonthBudgetMap: Map<YearMonth, Budget> = budgets.associateBy { it.getYearMonth() }

        val dayAmountMap = mutableMapOf<YearMonth, Double>()
        while (startYearMonth <= endYearMonth) {
            val monthBudget = yearMonthBudgetMap[startYearMonth] ?: Budget(startYearMonth.toMyString())
            dayAmountMap[startYearMonth] = getDayBudget(monthBudget)
            startYearMonth = startYearMonth.plusMonths(1)
        }
        val map = mutableMapOf<YearMonth, Int>()
        var date = startDate
        while (date <= endDate) {
            val keyMonth = YearMonth.of(date.year, date.month)
            if (map.containsKey(keyMonth)) {
                map[keyMonth] = map[keyMonth]!!.plus(1)
            } else {
                map[keyMonth] = 1
            }
            date = date.plusDays(1)
        }

        var amount = 0.00
        map.forEach { (yearMonth, day) ->
            amount += day * (dayAmountMap[yearMonth] ?: 0.00)
        }

        return amount
    }

    private fun getDayBudget(budget: Budget): Double {
        return budget.amount.toDouble() / YearMonth.of(budget.getYearMonth().year, budget.getYearMonth().monthValue)
            .lengthOfMonth()
    }

    private fun YearMonth.toMyString(): String = this.toString().replace("-", "")
}
