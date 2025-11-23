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
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        return database.insertReceipt(receipt, formattedDate)
    }

    fun getReceipts(): Result<List<Receipt>> {
        return database.getReceipts()
    }
}