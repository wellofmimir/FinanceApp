package com.example.financeapp.dailytipscreen
import com.example.financeapp.R

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun RandomTipSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    val randomTips = dailyTipScreenViewModel.likedTipsRandomlyOrdered.collectAsState()
    dailyTipScreenViewModel.getLikedTipsOrderedRandomly()

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Random favourite tip:",
            fontSize = 16.sp,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp, start = 12.dp)
        )

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        var scrollState = rememberScrollState()
        val tip = randomTips.value.firstOrNull()

        Text (
            text = tip?.dailyTip?.tip ?: "Eat fewer avocado toasts ;)",
            fontSize = 14.sp,
            color = colors.primary,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(3f)
                .verticalScroll(scrollState)
        )

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp, end = 12.dp)
                .fillMaxWidth()
        ) {
            Box (
                modifier = Modifier
                    .size(43.dp)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Image (
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "Herz",
                )
            }

            Text (
                text = if (tip == null) "" else "Tip No. ${tip.id}",
                color = colors.primary
            )
        }
    }
}