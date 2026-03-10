package studio.lemniscate.greeen.shopscreen

import studio.lemniscate.greeen.ui.theme.Emerald
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.safeDrawingPadding

@Composable
fun RemoveAdsSection (
    modifier: Modifier = Modifier,
    purchaseRequested: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Column (
        modifier = modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(top = 4.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = "$3.99",
            color = colors.primary,
            fontSize = typography.subtitle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(2f)
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        Box (
            modifier = Modifier
                .border (
                    width = 1.dp,
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White
                )
                .background (
                    color = Emerald,
                    shape = RoundedCornerShape(12.dp)
                )
                .fillMaxWidth(0.5f)
                .weight(3f)
                .clickable () {
                    purchaseRequested()
                },
            contentAlignment = Alignment.Center
        ) {
            Text (
                text = "Remove all ads",
                color = Color.White,
                fontSize = typography.subtitle
            )
        }
    }
}