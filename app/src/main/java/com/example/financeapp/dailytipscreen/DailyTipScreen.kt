package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.advertisement.InterstitialAdManager
import com.example.financeapp.BuildConfig

import android.content.Context
import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep

@Composable
fun DailyTipScreen(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    val activity = context as? Activity

    var interstitialAdCanBeShown by remember { mutableStateOf(false) }
    var newDailyTipCanBeShown by remember { mutableStateOf(false) }

    LaunchedEffect(interstitialAdCanBeShown) {
        activity?.let {

            dailyTipScreenViewModel.fetchDailyTip()

            if (dailyTipScreenViewModel.interstitialAdAfterDailyTipSeen()) {
                newDailyTipCanBeShown = true
                return@LaunchedEffect
            }

            if (!interstitialAdCanBeShown)
                return@LaunchedEffect

            InterstitialAdManager.instance.showInterstitial (
                activity = it,
                onAdClosed = {
                    dailyTipScreenViewModel.setInterstitialAdAfterDailyTipSeen()
                    newDailyTipCanBeShown = true
                },
                onAdFailed = {
                    newDailyTipCanBeShown = true
                }
            )
        }
    }

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background (
                color = colors.primary
            )
    ) {
        if (dailyTipScreenViewModel.newDailyTipAvailable()) {
            if (newDailyTipCanBeShown) {
                dailyTipScreenViewModel.resetNewDailyTipAvailable()

                DailyTipSection (
                    dailyTipScreenViewModel = dailyTipScreenViewModel
                )
            } else {
                AdTeaserSection (
                    onConfirmButtonClicked = {
                        interstitialAdCanBeShown = true
                    }
                )
            }
        } else {
            DailyTipSection (
                dailyTipScreenViewModel = dailyTipScreenViewModel
            )
        }

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            NumberOfTipsSection (
                modifier = modifier
                    .weight(1f),
                dailyTipScreenViewModel = dailyTipScreenViewModel
            )

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            RandomTipSection (
                modifier = modifier
                    .weight(1f),
                dailyTipScreenViewModel = dailyTipScreenViewModel
            )
        }

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        FavouriteTipsSection (
            dailyTipScreenViewModel = dailyTipScreenViewModel
        )
    }
}