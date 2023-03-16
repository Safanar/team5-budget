package budget

import java.time.LocalDate

class BudgetService(
    private val budgetRepo: BudgetRepo
) {

    fun query(startDate: LocalDate, endDate: LocalDate): Double {
        if (startDate.isAfter(endDate)) return 0.0
        val period = Period(startDate, endDate)
        return budgetRepo.getAll().sumByDouble { it.getAmount(period) }
    }

}
