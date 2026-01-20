package com.example.financeapp.dailytipscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun BadgesSection (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(25) {
            BadgeTile (
                title = "Ambitious reader",
                text = "You're doing great!",
                onSeeGift = {

                }
            )

            Spacer (
                modifier = Modifier
                    .padding(1.dp)
            )
        }
    }
}