package com.example.financeapp.settingsscreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.TutorialInformation
import com.example.financeapp.advertisement.AdSectionMiddleBanner
import com.example.financeapp.header.HeaderSectionViewModel

@Composable
fun SettingsScreen(headerSectionViewModel: HeaderSectionViewModel, settingsViewModel: SettingsViewModel, tutorialInformation: TutorialInformation) {

    SettingsSection (
        headerSectionViewModel = headerSectionViewModel,
        settingsViewModel = settingsViewModel
    )

    Spacer (
        modifier = Modifier
            .height(4.dp)
    )

    AdSectionMiddleBanner(tutorialInformation = tutorialInformation)
}