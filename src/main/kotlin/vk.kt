data class Transfer(val amount: Double, val cardType: CardType)

enum class CardType {
    MASTERCARD, MAESTRO, VISA, MIR, VK_PAY
}

class MoneyTransfer {

    private var dailySentAmount: Double = 0.0
    private var monthlySentAmount: Double = 0.0
    private var vkPaySentAmount: Double = 0.0

    fun sendMoney(transfer: Transfer): String {
        // Проверка лимитов
        when (transfer.cardType) {
            CardType.VK_PAY -> {
                if (transfer.amount > 15000) return "Сумма перевода не может превышать 15000 рублей за один раз."
                if (vkPaySentAmount + transfer.amount > 4000) return "Сумма переводов со счёта VK Pay не может превышать 4000 рублей в месяц."
                vkPaySentAmount += transfer.amount
            }
            else -> {
                if (dailySentAmount + transfer.amount > 150000) return "Сумма переводов по одной карте не может превышать 150000 рублей в сутки."
                if (monthlySentAmount + transfer.amount > 600000) return "Сумма переводов по одной карте не может превышать 600000 рублей в месяц."
                dailySentAmount += transfer.amount
                monthlySentAmount += transfer.amount
            }
        }

        // Рассчет комиссии
        val commission = calculateCommission(transfer)
        val totalAmount = transfer.amount + commission

        return "Перевод выполнен. Сумма перевода: ${transfer.amount} рублей. Комиссия: $commission рублей. Итого: $totalAmount рублей."
    }

    private fun calculateCommission(transfer: Transfer): Double {
        return when (transfer.cardType) {
            CardType.MASTERCARD, CardType.MAESTRO -> {
                if (transfer.amount >= 300 && transfer.amount <= 75000) {
                    0.0 // Без комиссии в рамках акции
                } else {
                    transfer.amount * 0.006 + 20 // 0.6% + 20 рублей
                }
            }
            CardType.VISA, CardType.MIR -> {
                if (transfer.amount < 35) {
                    35.0 // Минимальная комиссия
                } else {
                    transfer.amount * 0.007 // 0.75%
                }
            }
            CardType.VK_PAY -> {
                0.0 // Без комиссии для VK Pay
            }
            else -> {
                throw IllegalArgumentException("Неизвестный тип карты")
            }
        }
    }
}
