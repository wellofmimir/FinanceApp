package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurrencyRepository(private val database: FinanceAppDatabase) {
    companion object {
        private var instance: CurrencyRepository? = null

        fun getInstance(database: FinanceAppDatabase): CurrencyRepository {
            if (instance == null)
                instance = CurrencyRepository(database)

            return instance!!
        }
    }

    private val internCurrency = MutableStateFlow("")
    val currency = internCurrency.asStateFlow()

    fun getCurrency() {
        internCurrency.value = database.getCurrency()
    }

    fun setCurrency(currency: String) {
        database.setCurrency(currency)
    }
}