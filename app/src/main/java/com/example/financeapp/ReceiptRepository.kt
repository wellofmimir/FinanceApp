package com.example.financeapp

import androidx.compose.ui.res.integerResource

class ReceiptRepository private constructor(private val database: FinanceAppDatabase) {
    companion object {
        private var instance: ReceiptRepository? = null

        fun getInstance(database: FinanceAppDatabase): ReceiptRepository {
            if (instance == null)
                instance = ReceiptRepository(database)

            return instance!!
        }
    }

    fun insertReceipt(receipt: Receipt): Result<Long> {

        val currentDate = java.time.LocalDate.now()
        var formatter = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        formatter = java.time.format.DateTimeFormatter.ofPattern("MMM", java.util.Locale.ENGLISH)
        val abbreviatedCurrentMonth = currentDate.format(formatter)

        database.getMonth(abbreviatedCurrentMonth)
            .onSuccess {
                receipt.idMonth = it.toLong()
            }
            .onFailure {

            }

        return database.insertReceipt(receipt, formattedDate)
    }

    fun getReceipts(): Result<List<Receipt>> {
        return database.getReceipts()
    }

    fun getCurrentMonth(): String {

        val currentDate = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        return formattedDate
    }

    fun getReceiptsForLastMonths(numberOfLastMonths: Int): Result<List<Receipt>> {

        val currentMonth = getCurrentMonth()
        val receipts = mutableListOf<Receipt>()

        return database.getMonth(currentMonth)
            .mapCatching {
                for (monthOffset in 0 until numberOfLastMonths) {
                    val month = it - monthOffset.toLong()

                    database.getReceiptsForAMonth(month)
                        .onSuccess { list ->
                            receipts.addAll(list)
                        }
                        .onFailure {
                            // Fehler ignorieren oder loggen
                        }
                }

                receipts.toList()
            }
    }
}