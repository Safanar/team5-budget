package budget

import java.time.LocalDate

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
){

    fun getYearMonthDate() : LocalDate {
        return LocalDate.of(yearMonth.substring(0,4).toInt(), yearMonth.substring(5,6).toInt(), 1)!!
    }
}

