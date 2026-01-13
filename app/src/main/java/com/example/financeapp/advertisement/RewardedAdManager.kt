package com.example.financeapp.advertisement

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager (
    private val context: Context
) {
    private var rewardedAd: RewardedAd? = null

    var isLoading = false
        private set

    fun load(adUnitId: String) {

        if (isLoading || rewardedAd != null)
            return

        isLoading = true

        RewardedAd.load (
            context,
            adUnitId,
            AdRequest.Builder().build(),
            object: RewardedAdLoadCallback() {

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                    isLoading = false
                }
            }
        )
    }

    fun show (
        activity: Activity,
        onReward: () -> Unit,
        onClosed: () -> Unit
    ) {
        rewardedAd?.let { ad ->
            ad.fullScreenContentCallback = object: FullScreenContentCallback() {

                override fun onAdDismissedFullScreenContent() {
                    rewardedAd = null
                    onClosed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    rewardedAd = null
                    onClosed()
                }
            }

            ad.show(activity) {
                onReward()
            }
        }
    }

    fun isReady(): Boolean = rewardedAd != null
}