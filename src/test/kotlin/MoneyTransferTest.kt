import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class MoneyTransferTest {

    private lateinit var moneyTransfer: MoneyTransfer

    @Before
    fun setUp() {
        moneyTransfer = MoneyTransfer()
    }

    @Test
    fun testSendMoney_VKPay_Success() {
        val transfer = Transfer(4000.0, CardType.VK_PAY)
        val result = moneyTransfer.sendMoney(transfer)
        assertTrue(result.startsWith("Перевод выполнен"))
    }

    @Test
    fun testSendMoney_VKPay_ExceedsSingleTransferLimit() {
        val transfer = Transfer(16000.0, CardType.VK_PAY)
        val result = moneyTransfer.sendMoney(transfer)
        assertEquals("Сумма перевода не может превышать 15000 рублей за один раз.", result)
    }

    @Test
    fun testSendMoney_VKPay_ExceedsMonthlyLimit() {
        moneyTransfer.sendMoney(Transfer(3000.0, CardType.VK_PAY)) // 1-й перевод
        val transfer = Transfer(2000.0, CardType.VK_PAY) // 2-й перевод
        val result = moneyTransfer.sendMoney(transfer)
        assertEquals("Сумма переводов со счёта VK Pay не может превышать 4000 рублей в месяц.", result)
    }

    @Test
    fun testSendMoney_Maestro_Success() {
        val transfer = Transfer(500.0, CardType.MAESTRO)
        val result = moneyTransfer.sendMoney(transfer)
        assertTrue(result.startsWith("Перевод выполнен"))
    }
}
