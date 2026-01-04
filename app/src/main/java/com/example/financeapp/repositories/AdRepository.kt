package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase

class AdRepository private constructor (private val database: FinanceAppDatabase) {
    companion object {
        private var instance: AdRepository? = null

        fun getInstance(database: FinanceAppDatabase): AdRepository {
            if (instance == null)
                instance = AdRepository(database)

            return instance!!
        }
    }

    fun setRemoveAllAds() {
        database.setRemoveAllAds()
    }

    fun getRemoveAllAds(): Boolean {
        return database.getRemoveAllAds()
    }

    fun setInterstitialAdAfterReceiptSeen() {
        database.setInterstitialAdAfterReceiptSeen()
    }

    fun interstitialAdAfterReceiptSeen(): Boolean {
        return database.interstitialAdAfterReceiptSeen()
    }
}