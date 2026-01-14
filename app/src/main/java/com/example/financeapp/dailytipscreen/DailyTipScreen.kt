package com.example.financeapp.dailytipscreen

import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.network.DailyTip

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
import androidx.compose.foundation.layout.fillMaxSize
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

@Composable
fun DailyTipScreen(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    val activity = context as? Activity

    val newDailyTipAvailable by dailyTipScreenViewModel.newDailyTipAvailable.collectAsState()
    val imageToDailyTip by dailyTipScreenViewModel.imageToDailyTip.collectAsState()

    val dailyTip by dailyTipScreenViewModel.dailyTip.collectAsState()
    val likedTips by dailyTipScreenViewModel.likedTips.collectAsState()
    val currentlyLiked by dailyTipScreenViewModel.currentlyLiked.collectAsState()

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.fetchDailyTip()
        dailyTipScreenViewModel.getLikedTips()
    }

    var interstitialAdCanBeShown by remember { mutableStateOf(false) }
    var showTip by remember { mutableStateOf(false)}
    var showDialogWithImageToDailyTip by remember { mutableStateOf(false) }
    var temporaryTip by remember { mutableStateOf<DailyTip?>(null) }
    val dialogInteractionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interstitialAdCanBeShown) {
        activity?.let {
            dailyTipScreenViewModel.fetchDailyTip()

            if (!interstitialAdCanBeShown)
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
                    .fillMaxSize()
                    .clickable (
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
            if (dailyTipScreenViewModel.newDailyTipCanBeShown) {
                dailyTipScreenViewModel.resetNewDailyTipAvailable()

                DailyTipTile (
                    modifier = modifier,
                    currentlyLiked = currentlyLiked,
                    dailyTip = dailyTip.dailyTip,
                    onLiked = {
                        dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)
                    },
                    onSeeMoreClicked = {
                        showTip = true
                        temporaryTip = dailyTip.dailyTip
                    }
                )
            } else {
                AdTeaserSection (
                    onConfirmButtonClicked = {
                        interstitialAdCanBeShown = true
                    }
                )
            }
        } else {
            DailyTipTile (
                modifier = modifier,
                currentlyLiked = currentlyLiked,
                dailyTip = dailyTip.dailyTip,
                onLiked = {
                    dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)
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
            NumberOfTipsSection (
                modifier = modifier
                    .weight(1f),
                dailyTipScreenViewModel = dailyTipScreenViewModel
            )

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            if (newDailyTipAvailable) {
                if (dailyTipScreenViewModel.newDailyTipCanBeShown) {
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
                            text = "Your next tip is forming...\ncan you make it appear?",
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