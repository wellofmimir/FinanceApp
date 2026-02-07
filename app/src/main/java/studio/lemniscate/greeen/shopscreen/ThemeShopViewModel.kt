package studio.lemniscate.greeen.shopscreen
import studio.lemniscate.greeen.repositories.ShopRepository
import studio.lemniscate.greeen.billingmanager.BillingManager

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeShopViewModel (
    private val billingManager: BillingManager,
    private val shopRepository: ShopRepository
): ViewModel() {
    init {
        billingManager.setListener(object: BillingManager.Listener {
            override fun onPurchaseSuccess(idProduct: String) {
                purchasedThemes[idProduct] = true

                if (idProduct == "RemoveAllAds")
                    shopRepository.setRemoveAllAdsAsPurchased()
            }
        })

        billingManager.start()

        viewModelScope.launch {
            while (!billingManager.isReady) {
                delay(100)
            }

            billingManager.restorePurchases()
        }
    }

    private val purchasedThemes = mutableStateMapOf<String, Boolean>()

    fun getThemePurchased(theme: String): Boolean {
        val alreadyPurchased = purchasedThemes[theme] ?: shopRepository.getThemePurchased(theme)
        purchasedThemes[theme] = alreadyPurchased
        return alreadyPurchased
    }
    fun purchaseTheme(activity: Activity, theme: String) {
        shopRepository.purchaseTheme(activity, theme)
        getThemePurchased(theme)
        getAppliedTheme()
    }

    private val internAppliedTheme = MutableStateFlow(shopRepository.getAppliedTheme())
    val appliedTheme = internAppliedTheme.asStateFlow()

    fun setAppliedTheme(theme: String) {
        shopRepository.setAppliedTheme(theme)
        internAppliedTheme.value = theme
    }

    fun getAppliedTheme() {
        internAppliedTheme.value = shopRepository.getAppliedTheme()
    }

    fun purchaseRemoveAllAds(activity: Activity) {
        shopRepository.purchaseRemoveAllAds(activity)
    }
}