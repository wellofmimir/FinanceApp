package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase

class AdRepository(private val database: FinanceAppDatabase) {
    companion object {
        private var instance: AdRepository? = null

        fun getInstance(database: FinanceAppDatabase): AdRepository {
            if (instance == null)
                instance = AdRepository(database)

            return instance!!
        }
    }

    fun setInterstitialAdAfterReceiptSeen() {
        database.setInterstitialAdAfterReceiptSeen()
    }

    fun interstitialAdAfterReceiptSeen(): Boolean {
        return database.interstitialAdAfterReceiptSeen()
    }
}