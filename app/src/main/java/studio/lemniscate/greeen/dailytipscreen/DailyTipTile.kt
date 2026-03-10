package studio.lemniscate.greeen.dailytipscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.network.DailyTip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.heightIn

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.material3.Text

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import studio.lemniscate.greeen.ui.theme.LocalAppTypography


@Composable
fun DailyTipTile (
    modifier: Modifier = Modifier,
    currentlyLiked: Boolean,
    dailyTip: DailyTip,
    onLiked: () -> Unit,
    onSeeMoreClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .heightIn (175.dp, 190.dp)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding (start = 12.dp, top = 8.dp)
            .clickable (
            ) {
                onSeeMoreClicked()
            }
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding (end = 12.dp)
                .weight(1.5f)
        ) {
            Text (
                text = dailyTip.title,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontSize = typography.medium,
                modifier = Modifier
                    .weight(0.8f)
            )

            Box (
                modifier = Modifier
                    .size(43.dp)
                    .fillMaxWidth(0.2f)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onLiked()
                    },
                contentAlignment = Alignment.Center
            ) {
                Image (
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "Herz",
                    colorFilter = ColorFilter.tint(if (currentlyLiked) Color.Red else Color.Black)
                )
            }
        }

        Text (
            text = dailyTip.short,
            color = colors.primary,
            fontSize = typography.medium,
            modifier = Modifier
                .weight(2f)
                .padding(end = 12.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomStart
        ) {
            Text (
                text = "Tap to read the article.",
                color = colors.primary,
                fontSize = typography.small * 0.8f,
                maxLines = 1
            )
        }
    }
}