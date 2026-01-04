package com.example.financeapp.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import com.example.financeapp.TutorialInformation
import com.example.financeapp.advertisement.AdSectionMiddleBanner
import com.example.financeapp.advertisement.AdvertisementViewModel
import com.example.financeapp.header.HeaderSectionViewModel

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.advertisement.AdSectionLargeBanner

@Composable
fun SettingsScreen(headerSectionViewModel: HeaderSectionViewModel, advertisementViewModel: AdvertisementViewModel, settingsViewModel: SettingsViewModel, tutorialInformation: TutorialInformation) {

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

        if (advertisementViewModel.getRemoveAllAds())
            return@Column

        AdSectionLargeBanner (
            modifier = Modifier
                .weight(0.16f),
            tutorialInformation = tutorialInformation
        )
    }
}