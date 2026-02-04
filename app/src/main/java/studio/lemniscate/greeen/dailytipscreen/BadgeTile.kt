package studio.lemniscate.greeen.dailytipscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio

import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BadgeTile (
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    onSeeGift: () -> Unit,
    showGiftText: Boolean
) {
    val colors = LocalAppColors.current

    Column (
        modifier = modifier
            .height(150.dp)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 8.dp)
            .clickable (
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onSeeGift()
            }
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text (
                text = title,
                fontWeight = FontWeight.Bold,
                color = colors.primary,
                fontSize = 18.sp,
                modifier = Modifier.weight(5f)
            )

            Image (
                painter = painterResource(R.drawable.herzzumliken_foreground),
                contentDescription = "Herz",
                colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .size(44.dp)
                    .aspectRatio(1f)
            )
        }

        Spacer (
            modifier = Modifier
                .height(8.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f),
            contentAlignment = Alignment.TopStart
        ) {
            Text (
                text = text,
                color = colors.primary,
                fontSize = 16.sp,
                textAlign = TextAlign.Left
            )
        }

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.BottomStart
        ) {
            if (showGiftText) {
                Text (
                    text = "Tap to see your gift.",
                    color = colors.primary,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }
    }
}