package com.example.financeapp

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdSectionLargeBanner(tutorialInformation: TutorialInformation) {

    AndroidView (
        modifier = Modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdSectionMiddleBanner(tutorialInformation: TutorialInformation) {

    AndroidView (
        modifier = Modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
            }
        }
    )
}