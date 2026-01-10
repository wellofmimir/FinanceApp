package com.example.financeapp.dailytipscreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember


@Composable
fun DailyTipSection (
    modifier: Modifier = Modifier,
    dailyTipScreenViewModel: DailyTipScreenViewModel
) {
    val dailyTip by dailyTipScreenViewModel.dailyTip.collectAsState()
    val currentlyLiked by dailyTipScreenViewModel.currentlyLiked.collectAsState()
    var showTip by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.fetchDailyTip()
    }

    if (showTip) {
        DailyTipDialog (
            modifier = Modifier,
            dailyTip = dailyTip.dailyTip,
            currentlyLiked = currentlyLiked,
            onDismissRequest = {
                showTip = false
            }
        )
    }

    DailyTipTile (
        modifier = Modifier,
        currentlyLiked = currentlyLiked,
        dailyTip = dailyTip.dailyTip,
        onLiked = {
            dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)
        },
        onSeeMoreClicked = {
            showTip = true
        }
    )
}