package studio.lemniscate.greeen.dailytipscreen

import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.network.DailyTip
import studio.lemniscate.greeen.badges.BadgesViewModel

import android.content.Context
import android.app.Activity
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.notifications.DailyEvents
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel

private fun checkIfBadgeIsAvailable (
    currentlyLiked: Boolean,
    numberOfThingsLearned: Int,
    badgesViewModel: BadgesViewModel
) {
    if (currentlyLiked)
        return

    when (numberOfThingsLearned) {
        0   -> badgesViewModel.checkBadge(BadgeIdentifier.FIRST_DAILY_TIP_LIKED)
        6   -> badgesViewModel.checkBadge(BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED)
        29  -> badgesViewModel.checkBadge(BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED)
        59  -> badgesViewModel.checkBadge(BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED)
        89  -> badgesViewModel.checkBadge(BadgeIdentifier.NINETY_DAILY_TIPS_LIKED)
    }
}

@Composable
fun DailyTipScreen (
    modifier: Modifier = Modifier,
    dailyTipScreenViewModel: DailyTipScreenViewModel,
    shopViewModel: ThemeShopViewModel,
    badgesViewModel: BadgesViewModel,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    val activity = context as? Activity

    val isBadgeAvailable by badgesViewModel.isBadgeAvailable.collectAsState()

    val newDailyTipAvailable by dailyTipScreenViewModel.newDailyTipAvailable.collectAsState()
    val imageToDailyTip by dailyTipScreenViewModel.imageToDailyTip.collectAsState()

    val dailyTip by dailyTipScreenViewModel.dailyTip.collectAsState()
    val likedTips by dailyTipScreenViewModel.likedTips.collectAsState()
    val currentlyLiked by dailyTipScreenViewModel.currentlyLiked.collectAsState()

    val numberOfThingsLearned = likedTips.size
    var showBadges by remember { mutableStateOf(false) }

    val adremoverActive by shopViewModel.adRemoverPurchased.collectAsState()

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.fetchDailyTip()
        dailyTipScreenViewModel.getLikedTips()
    }

    var rewardedAdCanBeShown by remember { mutableStateOf(false) }
    var showTip by remember { mutableStateOf(false)}
    var showDialogWithImageToDailyTip by remember { mutableStateOf(false) }
    var temporaryTip by remember { mutableStateOf<DailyTip?>(null) }
    val dialogInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(rewardedAdCanBeShown) {
        activity?.let {
            dailyTipScreenViewModel.fetchDailyTip()

            if (!rewardedAdCanBeShown)
                return@LaunchedEffect

            dailyTipScreenViewModel.onWatchAd(activity = activity)
        }
    }

    if (showDialogWithImageToDailyTip) {
        Dialog (
            onDismissRequest = {
                showDialogWithImageToDailyTip = false
            },
            properties = DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            val imageBitmapFromTemporaryTip = dailyTipScreenViewModel.getImageBitmapFromDailyTip( temporaryTip ?: dailyTip.dailyTip)

            Image (
                bitmap = imageBitmapFromTemporaryTip,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .clickable(
                        indication = null,
                        interactionSource = dialogInteractionSource
                    ) {
                        showDialogWithImageToDailyTip = false

                        if (temporaryTip != null)
                            showTip = true
                    }
            )
        }
    }

    if (showBadges) {
        Dialog (
            onDismissRequest = {
                showBadges = false
            },
            properties = DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            BadgesSection (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f),
                badgesViewModel = badgesViewModel,
                onDismissRequest = {
                    showBadges = false
                }
            )
        }
    }

    if (showTip)
        DailyTipDialog (
            modifier = modifier,
            dailyTip = temporaryTip ?: dailyTip.dailyTip,
            currentlyLiked = currentlyLiked,
            onDismissRequest = {
                showTip = false
                temporaryTip = null
            },
            onShowImage = {
                showTip = false
                showDialogWithImageToDailyTip = true
            }
        )

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background (
                color = colors.primary
            )
    ) {
        if (newDailyTipAvailable) {
            if (dailyTipScreenViewModel.newDailyTipCanBeShown || adremoverActive) {
                DailyEvents.newDailyTip(false)

                DailyTipTile (
                    modifier = modifier,
                    currentlyLiked = currentlyLiked,
                    dailyTip = dailyTip.dailyTip,
                    onLiked = {
                        if (dailyTip.dailyTip.title.contains("Breathe in..."))
                            return@DailyTipTile

                        dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)

                        checkIfBadgeIsAvailable (
                            currentlyLiked = currentlyLiked,
                            numberOfThingsLearned = numberOfThingsLearned,
                            badgesViewModel = badgesViewModel,
                        )
                    },
                    onSeeMoreClicked = {
                        showTip = true
                        temporaryTip = dailyTip.dailyTip
                    }
                )
            } else {
                AdTeaserSection (
                    teaserText = "Your daily tip will be shown here after watching a quick ad.",
                    onConfirmButtonClicked = {
                        rewardedAdCanBeShown = true
                    }
                )
            }
        } else {
            DailyTipTile (
                modifier = modifier,
                currentlyLiked = currentlyLiked,
                dailyTip = dailyTip.dailyTip,
                onLiked = {
                    if (dailyTip.dailyTip.title.contains("Breathe in..."))
                        return@DailyTipTile

                    dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)

                    checkIfBadgeIsAvailable (
                        currentlyLiked = currentlyLiked,
                        numberOfThingsLearned = numberOfThingsLearned,
                        badgesViewModel = badgesViewModel,
                    )
                },
                onSeeMoreClicked = {
                    showTip = true
                    temporaryTip = dailyTip.dailyTip
                }
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
            if (isBadgeAvailable) {
                ViewBadgesSection (
                    modifier = modifier
                        .weight(1f)
                        .clickable() {
                            showBadges = true
                            badgesViewModel.resetBadgeAvailable()
                        }
                )
            } else {
                NumberOfTipsSection (
                    modifier = modifier
                        .weight(1f)
                        .clickable() {
                            showBadges = true
                        },
                    numberOfThingsLearned = numberOfThingsLearned
                )
            }


            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            if (newDailyTipAvailable) {
                if (dailyTipScreenViewModel.newDailyTipCanBeShown || adremoverActive) {
                    dailyTipScreenViewModel.resetNewDailyTipAvailable()

                    ImageSection (
                        imageBitmap = imageToDailyTip,
                        modifier = modifier
                            .weight(1f),
                        onImageClick = {
                            showDialogWithImageToDailyTip = true
                            temporaryTip = null
                        }
                    )
                } else {
                    Box (
                        contentAlignment = Alignment.Center,
                        modifier = modifier
                            .weight(1f)
                            .height(200.dp)
                            .fillMaxWidth()
                            .background (
                                color = colors.background,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border (
                                color = colors.surface,
                                width = 4.dp,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Text (
                            text = "Your next tip is forming...\nCan you make it appear?",
                            color = colors.secondary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                        )
                    }
                }
            } else {
                ImageSection (
                    imageBitmap = imageToDailyTip,
                    modifier = modifier
                        .weight(1f),
                    onImageClick = {
                        showDialogWithImageToDailyTip = true
                        temporaryTip = null
                    }
                )
            }
        }

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        FavouriteTipsSection (
            likedTips = likedTips,
            onFavouriteTipClicked = { favouriteTip ->
                temporaryTip = favouriteTip
                showTip = true
            }
        )
    }
}