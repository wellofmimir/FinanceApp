package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppTypography
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RecentlyCompletedGoalsSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    goalsSectionViewModel: GoalsSectionViewModel
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

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
            .padding(start = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Recently Completed:",
            color = colors.primary,
            fontSize = typography.button,
            fontStyle = FontStyle.Italic
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 6.dp)
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            state = listState
        ) {
            items(goals.take(goals.size)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                ) {
                    val bulletPointSize = with(LocalDensity.current) {
                        typography.small.toDp() * 0.8f
                    }

                    Image (
                        painter = painterResource(R.drawable.bulletpointfilled_foreground),
                        contentDescription = "Bulletpoint",
                        modifier = Modifier
                            .background (
                                color = colors.surface
                            )
                            .size(bulletPointSize),
                        colorFilter = ColorFilter.tint(colors.background)
                    )

                    Spacer (
                        modifier = Modifier
                            .width(12.dp)
                    )

                    Text (
                        fontSize = typography.small,
                        text = it.goal,
                        color = colors.textPrimary
                    )

                    Spacer (
                        modifier = Modifier
                            .width(4.dp)
                    )
                }
            }
        }

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )
    }
}