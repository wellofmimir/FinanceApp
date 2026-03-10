package studio.lemniscate.greeen.homescreen
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.Emerald
import studio.lemniscate.greeen.ui.theme.Pistachio

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import studio.lemniscate.greeen.homescreen.TutorialInformation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

@Composable
fun ShopSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    shopSectionClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Box (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_SHOP) 0.1f else 1.0f)
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (tutorialInformation.isActive)
                    return@clickable

                shopSectionClicked()
            }
    ) {
        Image (
            painter = painterResource(R.drawable.dollarsign_foreground),
            contentDescription = "Dollar",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 8.dp, top = 8.dp)
                .sizeIn(18.dp, 18.dp, 20.dp, 20.dp)
                .background (
                    color = colors.background,
                    shape = CircleShape
                ),
            colorFilter = ColorFilter.tint(colors.surface),
            contentScale = ContentScale.Fit
        )

        Text (
            text = "Shop",
            color = colors.textPrimary,
            fontSize = typography.button,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 2.dp)
        )
    }
}