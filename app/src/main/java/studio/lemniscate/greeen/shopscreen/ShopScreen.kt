package studio.lemniscate.greeen.shopscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import studio.lemniscate.greeen.advertisement.AdvertisementViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppColors

@Composable
fun ShopScreen (
    themeShopViewModel: ThemeShopViewModel,
    advertisementViewModel: AdvertisementViewModel,
    previewRequested: (theme: String) -> Unit,
    applyThemeRequested: (theme: String) -> Unit
) {
    val colors = LocalAppColors.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemeShopIntroSection (
            modifier = Modifier
                .weight(1f)
        )

        Spacer (
            modifier = Modifier
                .padding (
                    2.dp
                )
        )

        ThemeShopSection (
            modifier = Modifier
                .weight(5f),
            themeShopViewModel = themeShopViewModel,
            previewRequested = { theme ->
                previewRequested(theme)
            },
            applyThemeRequested = { theme ->
                applyThemeRequested(theme)
            },
            advertisementViewModel = advertisementViewModel
        )
    }
}