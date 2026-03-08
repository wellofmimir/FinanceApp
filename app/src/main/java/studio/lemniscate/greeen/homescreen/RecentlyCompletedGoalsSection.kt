package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RecentlyCompletedGoalsSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    goalsSectionViewModel: GoalsSectionViewModel
) {
    val colors = LocalAppColors.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp

    val fontSize = when {
        screenHeight <= 640 -> 22.sp
        screenHeight <= 720 -> 36.sp
        screenHeight <= 800 -> 48.sp
        else -> 48.sp
    }

    val bulletPointSize = when {
        screenHeight <= 640 -> 22.dp
        screenHeight <= 720 -> 36.dp
        screenHeight <= 800 -> 48.dp
        else -> 48.dp
    }

    val goals by goalsSectionViewModel.completedGoals.collectAsState()

    if (tutorialInformation.isActive)
        goalsSectionViewModel.getExampleGoals()
    else
        goalsSectionViewModel.getCompletedGoals()

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(start = 12.dp, top = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Recently Completed:",
            color = colors.primary,
            fontSize = fontSize * 0.35f,
            fontStyle = FontStyle.Italic
        )

        Spacer (
            modifier = Modifier
                .height(8.dp)
        )

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 12.dp)
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {
            items(goals.take(goals.size)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                ) {
                    Image (
                        painter = painterResource(R.drawable.bulletpointfilled_foreground),
                        contentDescription = "Bulletpoint",
                        modifier = Modifier
                            .background (
                                color = colors.surface
                            )
                            .size(bulletPointSize * 0.35f),
                        colorFilter = ColorFilter.tint(colors.background)
                    )

                    Spacer (
                        modifier = Modifier
                            .width(12.dp)
                    )

                    Text (
                        fontSize = fontSize * 0.35,
                        text = it.goal,
                        color = colors.textPrimary
                    )
                }
            }
        }
    }
}