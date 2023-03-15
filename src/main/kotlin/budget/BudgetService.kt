package budget

import java.time.LocalDate

class BudgetService(
    private val budgetRepo: BudgetRepo
){

    fun query(startDate: LocalDate, endDate: LocalDate){

    }

    fun getRange(start: LocalDate, end: LocalDate): List<Budget> {
        if (end.isBefore(start)) return emptyList()
        val budgetList: List<Budget> = budgetRepo.getAll()

        return budgetList.filter {
            if (it.yearMonth.length != 6) return@filter false
            val year = it.yearMonth.substring(0, 4).toInt()
            val month = it.yearMonth.substring(4, it.yearMonth.length).trimStart('0').toInt()
            year >= start.year && year <= end.year && month >= start.month.value && month <= end.month.value
        }
    }
}
