package com.example.financeapp.dailytipscreen

import com.example.financeapp.R
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

@Composable
fun DailyTipSection (
    modifier: Modifier = Modifier,
    dailyTipScreenViewModel: DailyTipScreenViewModel
) {
    val colors = LocalAppColors.current
    val dailyTip by dailyTipScreenViewModel.dailyTip.collectAsState()
    val currentlyLiked by dailyTipScreenViewModel.currentlyLiked.collectAsState()

    LaunchedEffect(Unit) {
        dailyTipScreenViewModel.fetchDailyTip()
    }

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(125.dp)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, top = 8.dp)
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 12.dp)
        ) {
            Text (
                text = dailyTip.dailyTip.title,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontSize = 18.sp
            )

            Box (
                modifier = Modifier
                    .size(43.dp)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        dailyTipScreenViewModel.toggleDailyTipLiked(dailyTip.dailyTip)
                    },
                contentAlignment = Alignment.Center
            ) {
                Image (
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "Herz",
                    colorFilter = ColorFilter.tint(if (currentlyLiked) Color.Red else Color.Black)
                )
            }
        }

        val scrollState = rememberScrollState()

        Text (
            text = dailyTip.dailyTip.tip,
            color = colors.primary,
            fontSize = 16.sp,
            modifier = Modifier
                .verticalScroll(scrollState)
        )
    }
}