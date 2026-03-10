package studio.lemniscate.greeen.repositories

import studio.lemniscate.greeen.database.FinanceAppDatabase

class AdRepository private constructor (
    private val database: FinanceAppDatabase
) {
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