package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
){

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        val yearMonthBudgetMap: Map<YearMonth, Budget> = getRange(
            start = YearMonth.from(startDate),
            end = YearMonth.from(endDate)
        ).associateBy { YearMonth.from(it.getYearMonthDate()) }


        val amountMap = getDayAmountOfEachMonth(startDate, endDate, yearMonthBudgetMap)
        val dayMap = getDayOfEachMonth(startDate, endDate)
        var amount = 0.00
        dayMap.forEach{ (yearMonth, day) ->
            amount += day* (amountMap[yearMonth] ?: 0.00)
        }

        return amount
    }

    fun YearMonth.toMyString():String = this.toString().replace("-", "")

    private fun getRange(start: YearMonth, end: YearMonth): List<Budget> {
        if (end.isBefore(start)) return emptyList()
        val budgetList: List<Budget> = budgetRepo.getAll()

        return budgetList.filter {
            val yearMonth = it.getYearMonth()
            val compareToEnd = yearMonth.compareTo(end)
            val compareToStart = yearMonth.compareTo(start)
            (compareToStart >= 0) && (compareToEnd <= 0)
        }
    }

    private fun getDayBudget(budget: Budget): Double {
      return budget.amount.toDouble()/YearMonth.from(budget.getYearMonth()).lengthOfMonth()
    }

    private fun getDayOfEachMonth(startDate: LocalDate, endDate: LocalDate):Map<YearMonth, Int> {
        val map = mutableMapOf<YearMonth, Int>()
        var date = startDate
        while(date <= endDate) {
            val keyMonth = YearMonth.from(date)
            if (map.containsKey(keyMonth)) {
                map[keyMonth] = map[keyMonth]!!.plus(1)
            } else {
                map[keyMonth] = 1
            }
            date = date.plusDays(1)
        }
        return map
    }

    private fun getDayAmountOfEachMonth(startDate: LocalDate, endDate: LocalDate, yearMonthBudgetMap: Map<YearMonth, Budget>) : Map<YearMonth, Double> {
        var yearMonth = YearMonth.from(startDate)
        val endYearMonth = YearMonth.from(endDate)
        val amountMap = mutableMapOf<YearMonth, Double>()
        while(yearMonth <= endYearMonth) {
            val monthBudget = yearMonthBudgetMap[yearMonth] ?: Budget(yearMonth.toMyString())
            amountMap[yearMonth] = getDayBudget(monthBudget)
            yearMonth = yearMonth.plusMonths(1)
        }
        return amountMap
    }

}
