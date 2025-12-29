package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FavouriteTipsSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

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

        repeat(5) {
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
                    text = "Today’s Most Popular Tips:",
                    color = colors.primary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )
        }
    }
}