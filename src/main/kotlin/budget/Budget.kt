package budget

import java.time.LocalDate

data class Budget(
    val yearMonth: String = "",
    val amount: Int = 0
){

    fun getYearMonthDate() : LocalDate {
        println(yearMonth.substring(0,4))
        println(yearMonth.substring(5,6))
        return LocalDate.of(yearMonth.substring(0,3).toInt(), yearMonth.substring(3,5).toInt(), 1)!!
    }
}
