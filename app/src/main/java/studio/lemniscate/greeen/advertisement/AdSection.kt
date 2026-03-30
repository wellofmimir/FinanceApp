package studio.lemniscate.greeen.advertisement

import studio.lemniscate.greeen.homescreen.TutorialInformation

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize

@Composable
fun AdSectionLargeBanner (
    modifier: Modifier = Modifier,
    suppressAd: Boolean,
    tutorialInformation: TutorialInformation
) {
    if (suppressAd)
        return

    Box (
        modifier = modifier
    ) {
        AndroidView (
            modifier = Modifier
                .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
                .fillMaxWidth()
                .fillMaxHeight(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.LARGE_BANNER)
                    adUnitId = "ca-app-pub-8967992746965159/8045470615" // Test-ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun AdSectionMiddleBanner (
    modifier: Modifier = Modifier,
    suppressAd: Boolean,
    tutorialInformation: TutorialInformation
) {
    if (suppressAd)
        return

    Box (
        modifier = modifier
    ) {
        AndroidView (
            modifier = modifier
                .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
                .fillMaxWidth()
                .fillMaxHeight(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-8967992746965159/4046515859" // Test-ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Composable
fun AdSectionSmallBanner (
    modifier: Modifier = Modifier,
    suppressAd: Boolean
) {
    if (suppressAd)
        return

    Box (
        modifier = modifier
            .fillMaxSize()
    ) {
        AndroidView (
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-8967992746965159/8181201983" // Test-ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}