package com.example.financeapp.dailytipscreen

import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.R
import com.example.financeapp.network.DailyTip

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DailyTipDialog (
    modifier: Modifier = Modifier,
    dailyTip: DailyTip,
    currentlyLiked: Boolean = false,
    onDismissRequest: () -> Unit
) {
    val colors = LocalAppColors.current

    Dialog (
        onDismissRequest = {
            onDismissRequest()
        },
        DialogProperties (
            usePlatformDefaultWidth = false
        )
    ) {
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = modifier
                .fillMaxSize()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 8.dp, horizontal = 12.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text (
                    text = dailyTip.title,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    fontSize = 24.sp
                )

                Box (
                    modifier = Modifier
                        .size(43.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image (
                        painter = painterResource(R.drawable.herzzumliken_foreground),
                        contentDescription = "Herz",
                        colorFilter = ColorFilter.tint(if (currentlyLiked) Color.Red else Color.Black)
                    )
                }
            }

            Spacer (
                modifier = Modifier
                    .padding(4.dp)
            )

            Text (
                text = "Today's category:",
                color = colors.primary,
                fontSize = 16.sp
            )

            Text (
                text = dailyTip.category,
                color = colors.primary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer (
                modifier = Modifier
                    .padding(4.dp)
            )

            val verticalScrollState = rememberScrollState()

            Text (
                text = dailyTip.tip,
                color = colors.primary,
                fontSize = 16.sp,
                modifier = Modifier
                    .verticalScroll(verticalScrollState)
                    .weight(1f)
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable() {
                        onDismissRequest()
                    },
                horizontalArrangement = Arrangement.End
            ) {
                Text (
                    text = "Close",
                    color = colors.primary,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .clickable () {
                            onDismissRequest()
                        }
                )
            }
        }
    }
}
