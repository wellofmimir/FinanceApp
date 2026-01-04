package com.example.financeapp.billingmanager

import android.content.Context
import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams

class BillingManager(context: Context) {
    interface Listener {
        fun onPurchaseSuccess(idProduct: String)
    }

    var listener: Listener? = null
        private set

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    var isReady = false
        private set

    private val billingClient = BillingClient
        .newBuilder(context)
        .setListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases?.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
        }
        .enablePendingPurchases()
        .build()

    fun start() {
        billingClient.startConnection(object: BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                isReady = result.responseCode == BillingClient.BillingResponseCode.OK
            }

            override fun onBillingServiceDisconnected() {
                isReady = false
            }
        })
    }

    fun clearListener() {
        listener = null
        isReady = false
    }

    fun restorePurchases() {
        if (!isReady)
            return

        billingClient.queryPurchasesAsync(BillingClient.ProductType.INAPP) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach { purchase ->
                    handlePurchase(purchase)
                }
            }
        }
    }

    fun buyTestProduct(activity: Activity) {

        if (!isReady)
            return

        val productDetailParameters = QueryProductDetailsParams
            .newBuilder()
            .setProductList (
                listOf (
                    QueryProductDetailsParams
                        .Product
                        .newBuilder()
                        .setProductId("android.test.purchased")
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(productDetailParameters) { _, productDetailsList ->
            val productDetails = productDetailsList.firstOrNull() ?:
                return@queryProductDetailsAsync

            val flowParameters = BillingFlowParams
                .newBuilder()
                .setProductDetailsParamsList (
                    listOf (
                        BillingFlowParams
                            .ProductDetailsParams
                            .newBuilder()
                            .setProductDetails(productDetails)
                            .build()

                    )
                )
                .build()

            billingClient.launchBillingFlow(activity, flowParameters)
        }
    }

    private fun handlePurchase(purchase: Purchase) {

        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED)
            return

        purchase.products.forEach { idProduct ->
            listener?.onPurchaseSuccess(idProduct)
        }

        val parameters = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(parameters) {}
    }
}
