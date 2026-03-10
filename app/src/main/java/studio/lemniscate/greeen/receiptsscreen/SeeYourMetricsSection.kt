package studio.lemniscate.greeen.receiptsscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SeeYourMetricsSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    onSeeYourMetricsSectionButtonClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current


    Row (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_SEE_METRICS_SECTION) 0.1f else 1.0f)
            .fillMaxWidth()
            .height(100.dp)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box (
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box (
                modifier = Modifier
                    .padding()
                    .sizeIn(64.dp, 64.dp, 75.dp, 75.dp)
                    .size(64.dp)
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
                    painter = painterResource(R.drawable.metricslogo_foreground),
                    contentDescription = "MetricsLogo",
                    modifier = Modifier
                        .sizeIn(64.dp, 64.dp, 75.dp, 75.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onSeeYourMetricsSectionButtonClicked()
                        },
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(colors.surface)
                )
            }
        }

        Box (
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text (
                text = "See Metrics",
                color = colors.primary,
                fontSize = typography.medium
            )
        }
    }
}