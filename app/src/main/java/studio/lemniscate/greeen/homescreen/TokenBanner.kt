package studio.lemniscate.greeen.homescreen
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.ui.theme.Pistachio
import studio.lemniscate.greeen.ui.theme.Emerald

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

@Composable
fun TokenBanner(modifier: Modifier = Modifier, goalsSectionViewModel: GoalsSectionViewModel, tutorialInformation: TutorialInformation) {

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
            )
            .padding (
                horizontal = 2.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tokenIdentifier = currentGoal?.tokenCount ?: 0
        val circleSize = 64.dp
        val strokeWidth = 2.dp

        for (i in 1..5) {
            Box (
                modifier = Modifier
                    .size(circleSize)
            ) {
                Canvas (
                    modifier = Modifier
                        .aspectRatio(1f)
                ) {
                    val strokePx = strokeWidth.toPx()
                    val radius = size.minDimension / 2 - strokePx / 2

                    drawCircle (
                        color = colors.primary,
                        radius = radius,
                        style = if (i <= tokenIdentifier) Fill else Stroke(strokePx)
                    )
                }
            }
        }
    }
}
