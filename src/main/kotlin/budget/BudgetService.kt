package budget

import java.time.LocalDate
import java.time.YearMonth

class BudgetService(
    private val budgetRepo: BudgetRepo
){

    fun query(startDate: LocalDate, endDate: LocalDate){

    }

    fun getRange(start: YearMonth, end: YearMonth): List<Budget> {
        if (end.isBefore(start)) return emptyList()
        val budgetList: List<Budget> = budgetRepo.getAll()

        return budgetList.filter {
            val yearMonth = it.getYearMonth()
            yearMonth.isAfter(start) && yearMonth.isBefore(end)
        }
    }
}
