package studio.lemniscate.greeen.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.advertisement.AdvertisementViewModel
import studio.lemniscate.greeen.header.HeaderSectionViewModel
import studio.lemniscate.greeen.advertisement.AdSectionLargeBanner
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen (
    headerSectionViewModel: HeaderSectionViewModel,
    shopViewModel: ThemeShopViewModel,
    settingsViewModel: SettingsViewModel,
    tutorialInformation: TutorialInformation
) {
    val adremoverActive by shopViewModel.adRemoverPurchased.collectAsState()

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        SettingsSection (
            modifier = Modifier
                .weight(1f),
            headerSectionViewModel = headerSectionViewModel,
            settingsViewModel = settingsViewModel
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        AdSectionLargeBanner (
            modifier = Modifier
                .weight(0.16f),
            suppressAd = adremoverActive,
            tutorialInformation = tutorialInformation
        )
    }
}