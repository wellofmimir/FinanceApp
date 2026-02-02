package studio.lemniscate.greeen.receiptsscreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.material3.Text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.homescreen.TutorialInformation


@Composable
fun LogReceiptSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    onLogReceiptButtonClicked: () -> Unit
) {
    val colors = LocalAppColors.current

    Row (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_TAKE_PICTURE) 0.1f else 1.0f)
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
                    painter = painterResource(R.drawable.kamera_foreground),
                    contentDescription = "Kamerasymbol",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(bottom = 2.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            onLogReceiptButtonClicked()
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
                text = "Log Receipt",
                color = colors.primary,
                fontSize = 18.sp
            )
        }
    }
}