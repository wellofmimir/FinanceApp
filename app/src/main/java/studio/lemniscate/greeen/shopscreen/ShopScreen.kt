package studio.lemniscate.greeen.shopscreen

import android.app.Activity
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.advertisement.AdSectionSmallBanner
import studio.lemniscate.greeen.homescreen.TutorialInformation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.advertisement.AdSectionMiddleBanner


@Composable
fun ShopScreen (
    themeShopViewModel: ThemeShopViewModel,
    previewRequested: (theme: String) -> Unit,
    applyThemeRequested: (theme: String) -> Unit
) {
    val colors = LocalAppColors.current
    val context = LocalContext.current

    val adremoverActive by themeShopViewModel.adRemoverPurchased.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.spacedBy (
            4.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemeShopIntroSection (
            modifier = Modifier
        )

        ThemeShopSection (
            modifier = Modifier
                .weight(4f),
            themeShopViewModel = themeShopViewModel,
            previewRequested = { theme ->
                previewRequested(theme)
            },
            applyThemeRequested = { theme ->
                applyThemeRequested(theme)
            }
        )

        if (!adremoverActive) {
            RemoveAdsSection (
                modifier = Modifier
                    .weight(1f),
                purchaseRequested = {
                    val activity = context as Activity
                    themeShopViewModel.purchaseRemoveAllAds(activity = activity)
                }
            )
        }
    }
}