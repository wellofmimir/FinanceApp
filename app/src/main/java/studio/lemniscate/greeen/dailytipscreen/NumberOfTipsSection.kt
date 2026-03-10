package studio.lemniscate.greeen.dailytipscreen
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import android.content.Context

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

@Composable
fun NumberOfTipsSection (
    modifier: Modifier = Modifier,
    numberOfThingsLearned: Int
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    val fontSizeMultiplicator = when (numberOfThingsLearned) {
        in 0..9 -> 2f
        in 10..99 -> 2f
        in 100..999 -> 2f
        in 1000..9999 -> 1.5f
        in 10000..99999 -> 1f
        else -> 0.75f
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = "Total Things You've\nLearned:",
            fontSize = typography.medium,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(2f)
                .align (
                    alignment = Alignment.Start
                )
        )


        Text (
            text = numberOfThingsLearned.toString(),
            fontSize = typography.title * fontSizeMultiplicator,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align (
                    alignment = Alignment.End
                )
                .weight(1f)
        )
    }
}