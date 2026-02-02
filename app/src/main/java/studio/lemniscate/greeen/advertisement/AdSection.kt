package studio.lemniscate.greeen.advertisement

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import studio.lemniscate.greeen.homescreen.TutorialInformation
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdSectionFullBanner (
    modifier: Modifier = Modifier,
    supressAd: Boolean,
    tutorialInformation: TutorialInformation
) {
    if (supressAd)
        return

    AndroidView (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .fillMaxWidth()
            .height(50.dp),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.FULL_BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdSectionLargeBanner (
    modifier: Modifier = Modifier,
    supressAd: Boolean,
    tutorialInformation: TutorialInformation
) {
    if (supressAd)
        return

    AndroidView (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.LARGE_BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdSectionMiddleBanner (
    modifier: Modifier = Modifier,
    supressAd: Boolean,
    tutorialInformation: TutorialInformation
) {
    if (supressAd)
        return

    AndroidView (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

@Composable
fun AdSectionSmallBanner (
    modifier: Modifier = Modifier,
    supressAd: Boolean
) {
    if (supressAd)
        return

    AndroidView (
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = "ca-app-pub-3940256099942544/6300978111" // Test-ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}