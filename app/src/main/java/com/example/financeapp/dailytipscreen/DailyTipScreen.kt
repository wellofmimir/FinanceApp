package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DailyTipScreen(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background (
                color = colors.primary
            )
    ) {
        AdTeaserSection (
        )

        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        AdTeaserSection (

        )
    }
}