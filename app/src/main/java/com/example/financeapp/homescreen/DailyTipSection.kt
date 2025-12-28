package com.example.financeapp.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun DailyTipSection(modifier: Modifier = Modifier, context: Context = LocalContext.current, dailyTipSectionClicked: () -> Unit) {

    val colors = LocalAppColors.current

    Box (
        modifier = modifier
            .clickable {
                dailyTipSectionClicked()
            }
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = "Your daily finance tip is ready! Hooray!",
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
    }
}


