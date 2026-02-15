package studio.lemniscate.greeen.shopscreen
import studio.lemniscate.greeen.repositories.ShopRepository
import studio.lemniscate.greeen.billingmanager.BillingManager

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import studio.lemniscate.greeen.notifications.DailyEvents

class ThemeShopViewModel (
    private val billingManager: BillingManager,
    private val shopRepository: ShopRepository
): ViewModel() {
    init {
        billingManager.setListener(object: BillingManager.Listener {
            override fun onPurchaseSuccess(idProduct: String) {
                if (idProduct == "adremover") {
                    internAdRemoverPurchased.update { currentState ->
                        true
                    }
                } else {
                    internPurchasedThemes.update { current ->
                        current + (idProduct to true)
                    }
                }
            }
        })

        billingManager.start()

        viewModelScope.launch {
            while (!billingManager.isReady) {
                delay(100)
            }

            billingManager.restorePurchases()
        }

        viewModelScope.launch {
            ShopEvents.newThemePurchased.collect() { theme ->
                internAppliedTheme.value = theme
                internPurchasedThemes.update { current ->
                    current + (theme to true)
                }
            }
        }

        viewModelScope.launch {
            ShopEvents.adRemoverPurchased.collect() {
                internAdRemoverPurchased.value = true
            }
        }
    }


    private var internPurchasedThemes = MutableStateFlow<Map<String, Boolean> >(emptyMap())
    val purchasedThemes = internPurchasedThemes.asStateFlow()

    fun purchaseTheme(activity: Activity, theme: String) {
        shopRepository.purchaseTheme(activity, theme)
    }

    private val internAppliedTheme = MutableStateFlow(shopRepository.getAppliedTheme())
    val appliedTheme = internAppliedTheme.asStateFlow()

    fun setAppliedTheme(theme: String) {
        shopRepository.setAppliedTheme(theme)
        internAppliedTheme.value = theme
    }

    private var internAdRemoverPurchased = MutableStateFlow(shopRepository.getRemoveAllAds())
    val adRemoverPurchased = internAdRemoverPurchased.asStateFlow()


    fun getAppliedTheme() {
        internAppliedTheme.value = shopRepository.getAppliedTheme()
    }

    fun hasThemeBeenPurchased(theme: String): Boolean {
        if (theme == "charcoaltheme")
            return true

        return shopRepository.getThemePurchased(theme)
    }

    fun purchaseRemoveAllAds(activity: Activity) {
        shopRepository.purchaseRemoveAllAds(activity)
    }
}