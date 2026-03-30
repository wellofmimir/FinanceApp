package studio.lemniscate.greeen.advertisement

import android.app.Activity
import android.content.Context
import android.util.Log

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.LoadAdError

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class InterstitialAdManager private constructor() {

    private var interstitialAd: InterstitialAd? = null

    companion object {
        val instance: InterstitialAdManager by lazy {
            InterstitialAdManager()
        }

        private const val TAG = "Interstitial AdManager"
        private const val TEST_INTERSTITIAL_ID = "ca-app-pub-8967992746965159/4924483252"
    }

    fun initialize(context: Context) {
        MobileAds.initialize(context) {
            Log.d(TAG, "Mobile ads initialized")
        }
    }

    fun loadInterstitial(context: Context, adUnitId: String = TEST_INTERSTITIAL_ID) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load (
            context,
            adUnitId,
            adRequest,
            object: InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Failed loading interstitial: ${error.message}")
                    interstitialAd = null
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    Log.d(TAG, "Interstitial loaded successfully")
                }
            }
        )
    }

    fun showInterstitial (
        activity: Activity,
        onAdClosed: (() -> Unit)? = null,
        onAdFailed: (() -> Unit)? = null
    ) {
        val ad = interstitialAd

        if (ad == null) {
            Log.d(TAG, "Interstitial not ready yet.")
            onAdFailed?.invoke()
            loadInterstitial(activity)
            return
        }

        ad.fullScreenContentCallback = object: FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed")
                interstitialAd = null
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad closed")
                onAdClosed?.invoke()
                loadInterstitial(activity)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.e(TAG, "Ad failed to show: ${error.message}")
                onAdFailed?.invoke()
                loadInterstitial(activity)
            }
        }

        ad.show(activity)
    }
}