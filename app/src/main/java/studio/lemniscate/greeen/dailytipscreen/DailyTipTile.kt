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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.material3.Text

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color


@Composable
fun DailyTipTile (
    modifier: Modifier = Modifier,
    currentlyLiked: Boolean,
    dailyTip: DailyTip,
    onLiked: () -> Unit,
    onSeeMoreClicked: () -> Unit
) {
    val colors = LocalAppColors.current

    Column (
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .height (
                height = 150.dp
            )
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding (
                start = 12.dp,
                top = 8.dp
            )
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
                .padding (
                    end = 12.dp
                )
                .weight(1f)
        ) {
            Text (
                text = dailyTip.title,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontSize = 18.sp,
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
            fontSize = 16.sp,
            modifier = Modifier
                .weight(2f)
        )
    }
}