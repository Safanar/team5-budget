package budget

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class BudgetServiceTest {

    @InjectMockKs
    private lateinit var underTest: BudgetService

    private lateinit var budgetRepo: BudgetRepo

    @Before
    fun startUp(){
        budgetRepo = mockk()
        underTest = BudgetService(
            budgetRepo
        )
    }

    @Test
    fun one_month(){

    }

    @Test
    fun one_month(){

    }
}
