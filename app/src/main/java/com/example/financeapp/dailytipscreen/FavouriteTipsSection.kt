package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.network.DailyTip

@Composable
fun FavouriteTipsSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel) {

    val colors = LocalAppColors.current
    val favouriteTips = dailyTipScreenViewModel.likedTips.collectAsState()
    var showTip by remember { mutableStateOf(false) }
    var temporaryTip by remember { mutableStateOf<DailyTip?>(null) }

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.getLikedTips()
    }

    if (showTip) {
        DailyTipDialog (
            modifier = modifier,
            currentlyLiked = true,
            dailyTip =  temporaryTip ?: DailyTip("", "", "", ""),
            onDismissRequest = {
                showTip = false
            }
        )
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        ) {
        Box (
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
        ) {
            Text (
                text = "Your Favourite Tips:",
                color = colors.primary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = colors.primary,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            state = listState
        ) {
            items (
                items =favouriteTips.value.take(favouriteTips.value.size)
            ) { likedTip ->

                DailyTipTile (
                    modifier = Modifier,
                    currentlyLiked = true,
                    dailyTip = likedTip.dailyTip,
                    onLiked = {},
                    onSeeMoreClicked = {
                        temporaryTip = likedTip.dailyTip
                        showTip = true
                    }
                )

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
        }
    }
}