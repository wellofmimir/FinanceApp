package com.example.financeapp.repositories
import com.example.financeapp.billingmanager.BillingManager
import com.example.financeapp.database.FinanceAppDatabase
import android.app.Activity

class ShopRepository private constructor (private val database: FinanceAppDatabase, private val billingManager: BillingManager) {

    companion object {
        private var instance: ShopRepository? = null

        fun getInstance(database: FinanceAppDatabase, billingManager: BillingManager): ShopRepository {
            if (instance == null) {
                instance = ShopRepository(database, billingManager)
            }

            return instance!!
        }
    }

    fun getThemePurchased(theme: String): Boolean {
        return database.getThemePurchased(theme)
    }
    fun purchaseTheme(activity: Activity, theme: String) {
        billingManager.clearListener()

        billingManager.setListener(object: BillingManager.Listener {
            override fun onPurchaseSuccess(idProduct: String) {
                database.setThemePurchased(idProduct)
            }
        })

        billingManager.start()
        billingManager.buyTestProduct(activity)
    }
}