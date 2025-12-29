package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import android.content.Context

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.Text

@Composable
fun NumberOfTipsSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Total Things You've\nLearned:",
            fontSize = 16.sp,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )

        Spacer (
            modifier = Modifier
                .padding(24.dp)
        )

        Text (
            text = "999",
            fontSize = 80.sp,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align (
                    alignment = Alignment.End
                )
        )
    }
}