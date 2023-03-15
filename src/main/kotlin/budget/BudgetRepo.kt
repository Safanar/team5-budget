package budget

class BudgetRepo {

    fun getAll(): List<Budget> {
        return listOf(
            Budget("202303", 310),
            Budget("202304", 3000)
        )
    }
}
