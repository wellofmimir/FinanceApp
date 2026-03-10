package studio.lemniscate.greeen.dailytipscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image

import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

@Composable
fun AdTeaserSection (
    modifier: Modifier = Modifier,
    teaserText: String,
    onConfirmButtonClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(125.dp)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Column (
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.75f)
        ) {
            Text (
                text = teaserText,
                color = colors.primary,
                fontSize = typography.medium,
                fontWeight = FontWeight.Bold
            )

            Spacer (
                modifier = Modifier
                    .height(12.dp)
            )

            Text (
                text = "Ads help us keep Greeen free!",
                color = colors.primary,
                fontSize = typography.medium,
                fontWeight = FontWeight.Normal
            )
        }

        Box (
            modifier = Modifier
                .size(64.dp)
                .fillMaxWidth(0.25f)
                .clip(CircleShape)
                .background (
                    color = colors.background,
                    shape = CircleShape
                )
                .border (
                    width = 1.dp,
                    color = colors.background,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image (
                painter = painterResource(R.drawable.doppelpfeileruntersymbol_foreground),
                contentDescription = "Doppelpfeile",
                modifier = Modifier
                    .size(32.dp)
                    .background (
                        color = colors.background,
                        shape = CircleShape
                    )
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onConfirmButtonClicked()
                    },
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(colors.surface)
            )
        }
    }
}