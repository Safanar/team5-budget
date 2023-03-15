package budget

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class BudgetServiceTest {

    @InjectMockKs
    private lateinit var underTest: BudgetService

    private lateinit var budgetRepo: BudgetRepo

    private fun givenFakeBudget() {
        every { budgetRepo.getAll() } returns listOf(
            Budget("202303", 310),
            Budget("202304", 3000)
        )
    }

    private fun shouldBeBudget(startDate: LocalDate, endDate: LocalDate, budget: Double) {
        val actual = underTest.query(startDate, endDate)
        assertEquals(budget,actual)
    }

    @Before
    fun startUp(){
        budgetRepo = mockk()
        givenFakeBudget()
        underTest = BudgetService(
            budgetRepo
        )
    }

    @Test
    fun one_month(){
        shouldBeBudget(
            LocalDate.of(2023,3,1),
            LocalDate.of(2023,3,31),
            310.00
        )
    }

    @Test
    fun over_month(){
        shouldBeBudget(
            LocalDate.of(2023,3,31),
            LocalDate.of(2023,4,2),
            210.00
        )
    }

    @Test
    fun not_full_month(){
        shouldBeBudget(
            LocalDate.of(2023,3,1),
            LocalDate.of(2023,3,2),
            20.00
        )
    }

    @Test
    fun invited_date_range(){
        shouldBeBudget(
            LocalDate.of(2023,3,2),
            LocalDate.of(2023,3,1),
            0.00
        )
    }

    @Test
    fun no_month_budget(){
        shouldBeBudget(
            LocalDate.of(2023,5,1),
            LocalDate.of(2023,5,3),
            0.00
        )
    }


}
