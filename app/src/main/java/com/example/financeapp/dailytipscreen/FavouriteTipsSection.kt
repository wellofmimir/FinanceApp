package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.R

@Composable
fun FavouriteTipsSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel) {

    val colors = LocalAppColors.current
    val favouriteTips = dailyTipScreenViewModel.likedTips.collectAsState()

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.getLikedTips()
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
            items(favouriteTips.value.take(favouriteTips.value.size)) {likedTip ->
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background (
                            color = colors.secondary,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    val scrollState = rememberScrollState()

                    Text (
                        text = likedTip.dailyTip.tip,
                        color = colors.primary,
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .weight(3f)
                            .padding(top = 18.dp, start = 12.dp)
                    )

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Box (
                            modifier = Modifier
                                .size(43.dp)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image (
                                painter = painterResource(R.drawable.herzzumliken_foreground),
                                contentDescription = "Herz"
                            )
                        }

                        Box (
                            modifier = Modifier
                                .padding(end = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text (
                                text = "Tip no. " + likedTip.id.toString(),
                                color = colors.primary
                            )
                        }
                    }
                }

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )
            }
        }
    }
}