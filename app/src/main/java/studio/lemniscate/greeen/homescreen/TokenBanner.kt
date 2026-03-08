package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.Modifier

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import studio.lemniscate.greeen.R


@Composable
fun TokenBanner (
    modifier: Modifier = Modifier,
    goalsSectionViewModel: GoalsSectionViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val currentGoal by goalsSectionViewModel.currentGoal.collectAsState()

    LaunchedEffect(Unit) {
        goalsSectionViewModel.getCurrentGoal()
    }

    Row (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_TOKEN_BANNER) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tokenIdentifier = currentGoal?.tokenCount ?: 0

        for (i in 1..5) {
            Image (
                modifier = Modifier
                    .background (
                        shape = CircleShape,
                        color = Color.Transparent
                    )
                    .border (
                        width = if (i <= tokenIdentifier) 0.dp else 1.dp,
                        shape = CircleShape,
                        color = if (i <= tokenIdentifier) colors.secondary else colors.primary
                    )
                    .clip(CircleShape),
                painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                colorFilter = ColorFilter.tint(if (i <= tokenIdentifier) colors.primary else colors.secondary),
                contentDescription = "RingBild"
            )
        }
    }
}
