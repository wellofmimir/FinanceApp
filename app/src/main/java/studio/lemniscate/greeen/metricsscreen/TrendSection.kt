package studio.lemniscate.greeen.metricsscreen

import androidx.compose.foundation.background
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import studio.lemniscate.greeen.ui.theme.LocalAppTypography


@Composable
fun TrendSection (
    modifier: Modifier = Modifier,
    trend: String
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Box (
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
            .background (
                color = colors.primary
            ),
        contentAlignment = Alignment.Center
    ) {
        val verticalScroll = rememberScrollState()

        Text (
            modifier = Modifier
                .padding(4.dp)
                .verticalScroll(verticalScroll),
            text = trend,
            color = colors.secondary,
            fontSize = typography.medium,
            textAlign = TextAlign.Center
        )
    }
}