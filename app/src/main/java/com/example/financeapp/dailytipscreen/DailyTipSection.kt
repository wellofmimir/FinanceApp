package com.example.financeapp.dailytipscreen

import com.example.financeapp.R
import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.network.DailyTip

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DailyTipSection (
    modifier: Modifier = Modifier,
    dailyTipScreenViewModel: DailyTipScreenViewModel,
    context: Context = LocalContext.current,
) {
    val colors = LocalAppColors.current
    val dailyTip by dailyTipScreenViewModel.dailyTip.collectAsState()

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(125.dp)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, top = 16.dp)
    ) {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(4f)
                    .padding(start = 12.dp)
        ) {
            Text (
                text = dailyTip.title,
                color = colors.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer (
                modifier = Modifier
                    .padding(6.dp)
            )

            Text (
                text = dailyTip.tip,
                color = colors.primary,
                fontSize = 16.sp
            )
        }

        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            Box (
                modifier = Modifier
                    .size(43.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "Herz"
                )
            }
        }
    }
}