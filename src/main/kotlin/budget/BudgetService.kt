package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        val yearMonthBudgetMap: Map<YearMonth, Budget> = getRange(
            start = YearMonth.from(startDate),
            end = YearMonth.from(endDate)
        ).associateBy { it.getYearMonth() }

        var yearMonth = YearMonth.from(startDate)
        val endYearMonth = YearMonth.from(endDate)
        val dayAmountMap = mutableMapOf<YearMonth, Double>()
        while (yearMonth <= endYearMonth) {
            val monthBudget = yearMonthBudgetMap[yearMonth] ?: Budget(yearMonth.toMyString())
            dayAmountMap[yearMonth] = getDayBudget(monthBudget)
            yearMonth = yearMonth.plusMonths(1)
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

    private fun getRange(start: YearMonth, end: YearMonth): List<Budget> {
        return budgetRepo.getAll().filter {
            val yearMonth = it.getYearMonth()
            (yearMonth >= start) && (yearMonth <= end)
        }
    }

    private fun getDayBudget(budget: Budget): Double {
        return budget.amount.toDouble() / YearMonth.of(budget.getYearMonth().year, budget.getYearMonth().monthValue)
            .lengthOfMonth()
    }

    private fun YearMonth.toMyString(): String = this.toString().replace("-", "")
}
