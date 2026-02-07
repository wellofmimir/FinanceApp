package studio.lemniscate.greeen.repositories
import studio.lemniscate.greeen.billingmanager.BillingManager
import studio.lemniscate.greeen.database.FinanceAppDatabase
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
        billingManager.buyTestProduct(activity, theme)
    }

    fun purchaseRemoveAllAds(activity: Activity) {
        billingManager.clearListener()

        billingManager.setListener(object: BillingManager.Listener {
            override fun onPurchaseSuccess(idProduct: String) {
                database.setRemoveAllAds()
            }
        })

        billingManager.start()
        billingManager.buyTestProduct(activity, "adRemover")
    }

    fun setRemoveAllAdsAsPurchased() {
        database.setRemoveAllAds()
    }

    fun setAppliedTheme(theme: String) {
        database.setAppliedTheme(theme)
    }

    fun getAppliedTheme(): String {
        return database.getAppliedTheme()
    }
}