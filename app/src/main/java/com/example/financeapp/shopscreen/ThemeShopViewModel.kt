package com.example.financeapp.shopscreen
import com.example.financeapp.repositories.ShopRepository
import com.example.financeapp.billingmanager.BillingManager

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.app.Activity

class ThemeShopViewModel(private val billingManager: BillingManager, private val shopRepository: ShopRepository): ViewModel() {

    init {
        billingManager.setListener(object: BillingManager.Listener {
            override fun onPurchaseSuccess(idProduct: String) {
                purchasedThemes[idProduct] = true
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

        return true //TODO: Wieder einkommentieren
        //return alreadyPurchased
    }
    fun purchaseTheme(activity: Activity, theme: String) {
        shopRepository.purchaseTheme(activity, theme)
        getThemePurchased(theme)
    }
}