package com.example.financeapp.receiptsscreen

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.financeapp.MainActivityViewModel
import com.example.financeapp.TutorialInformation
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.advertisement.InterstitialAdManager

@Composable
fun ReceiptScreen(onReceiptAdded:() -> Unit, receiptSectionsViewModel: ReceiptSectionsViewModel, mainActivityViewModel: MainActivityViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    var timespan by remember { mutableStateOf(Timespan.THIS_MONTH) }
    val activity = context as? Activity
    var receiptAdded by remember { mutableStateOf(false) }

    LaunchedEffect(receiptAdded) {
        activity?.let {
            if (!receiptAdded)
                return@LaunchedEffect

            if (!mainActivityViewModel.getReceiptsTutorialDone())
                return@LaunchedEffect

            if (receiptSectionsViewModel.interstitialAdAfterReceiptSeen())
                return@LaunchedEffect

            InterstitialAdManager.instance.showInterstitial (
                activity = it,
                onAdClosed = {
                    receiptAdded = false
                    receiptSectionsViewModel.setInterstitialAdAfterReceiptSeen()
                },
                onAdFailed = {
                }
            )
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SinceWhenSection (
            onCurrentMonth = {
                timespan = it
            },
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AverageSpentSection (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                timespan = timespan,
                receiptAdded = {
                    receiptAdded = true
                    onReceiptAdded()
                },
                onDismissRequest = {

                },
                receiptSectionsViewModel = receiptSectionsViewModel,
                tutorialInformation = tutorialInformation
            )

            ExpensesOverviewSection (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                timespan,
                receiptSectionsViewModel,
                tutorialInformation = tutorialInformation
            )
        }

        ReceiptLogSection (
            modifier = Modifier
                .fillMaxHeight(0.8f),
            timespan = timespan,
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        AdSectionLargeBanner (
            tutorialInformation = tutorialInformation
        )
    }
}
