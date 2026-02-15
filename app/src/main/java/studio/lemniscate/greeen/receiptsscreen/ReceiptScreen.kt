package studio.lemniscate.greeen.receiptsscreen

import android.app.Activity
import android.content.Context

import studio.lemniscate.greeen.MainActivityViewModel
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.advertisement.AdSectionLargeBanner
import studio.lemniscate.greeen.advertisement.AdvertisementViewModel
import studio.lemniscate.greeen.advertisement.InterstitialAdManager
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.metricsscreen.MetricsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import studio.lemniscate.greeen.metricsscreen.MetricsScreenViewModel
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel

@Composable
fun ReceiptScreen (
    onReceiptAdded:() -> Unit,
    onAddReceiptMenuOpened: () -> Unit,
    onAddReceiptMenuClosed: () -> Unit,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    mainActivityViewModel: MainActivityViewModel,
    shopViewModel: ThemeShopViewModel,
    metricsScreenViewModel: MetricsScreenViewModel,
    badgesViewModel: BadgesViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    var timespan by remember { mutableStateOf(Timespan.THIS_MONTH) }
    val activity = context as? Activity
    var receiptAdded by remember { mutableStateOf(false) }
    var addReceiptMenuExpanded by remember { mutableStateOf(false) }
    val metricsScreenIsShown by metricsScreenViewModel.metricsScreenIsShown.collectAsState()
    val adremoverActive by shopViewModel.adRemoverPurchased.collectAsState()

    LaunchedEffect(receiptAdded) {
        activity?.let {
            if (!receiptAdded)
                return@LaunchedEffect

            if (!mainActivityViewModel.getReceiptsTutorialDone())
                return@LaunchedEffect

            if (receiptSectionsViewModel.interstitialAdAfterReceiptSeen())
                return@LaunchedEffect

            if (adremoverActive)
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

    if (addReceiptMenuExpanded || receiptSectionsViewModel.showAddReceiptSection.collectAsState().value) {
        AddReceiptMenu (
            onDismissRequest = {
                addReceiptMenuExpanded = false
                receiptSectionsViewModel.getReceipts()
                receiptSectionsViewModel.closeAddReceiptSection()
                onAddReceiptMenuClosed()
            },
            onReceiptSaved = {
                receiptAdded = true
                badgesViewModel.checkBadge(BadgeIdentifier.FIRST_RECEIPT)
                onReceiptAdded()
            },
            receiptSectionsViewModel = receiptSectionsViewModel
        )

        onAddReceiptMenuOpened()
        return
    }

    if (metricsScreenIsShown) {
        MetricsScreen (
            modifier = Modifier
                .fillMaxSize(0.5f),
            receiptSectionsViewModel = receiptSectionsViewModel,
            metricsScreenViewModel = metricsScreenViewModel,
            shopViewModel = shopViewModel,
            tutorialInformation = tutorialInformation,
            onReturn = {
                metricsScreenViewModel.resetMetricsScreenIsShown()
            }
        )

        metricsScreenViewModel.setMetricsScreenIsShown()
        return
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            LogReceiptSection (
                modifier = Modifier
                    .weight(1f),
                tutorialInformation = tutorialInformation,
                onLogReceiptButtonClicked = {
                    addReceiptMenuExpanded = true
                    receiptSectionsViewModel.showAddReceiptSection()
                }
            )

            SeeYourMetricsSection (
                modifier = Modifier
                    .weight(1f),
                tutorialInformation = tutorialInformation,
                onSeeYourMetricsSectionButtonClicked = {
                    metricsScreenViewModel.setMetricsScreenIsShown()
                    addReceiptMenuExpanded = false
                }
            )
        }

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

        SinceWhenSection (
            onCurrentMonth = {
                timespan = it
            },
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        ReceiptLogSection (
            modifier = Modifier
                .fillMaxHeight()
                .weight(4f),
            timespan = timespan,
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        AdSectionLargeBanner (
            modifier = Modifier
                .weight(1f),
            suppressAd = adremoverActive,
            tutorialInformation = tutorialInformation
        )
    }
}
