package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text

import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalsSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    goalsSectionViewModel: GoalsSectionViewModel,
    onAddNewGoalMenuRequested: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    val goals by goalsSectionViewModel.goals.collectAsState()
    var menuOpen by remember { mutableStateOf(false) }
    var goalIdToDelete by remember { mutableStateOf(0) }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_CURRENT_GOALS) 0.1f else 1.0f)
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp)
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "Stuff you're working on:",
                color = colors.textPrimary,
                fontSize = typography.subtitle,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = colors.surface
                    )
                    .padding(start = 18.dp)
                    .weight(6f)
            )

            Image (
                painter = painterResource(R.drawable.pluszeichen_standard_pistachio_foreground),
                contentDescription = "Pluszeichen",
                modifier = Modifier
                    .weight(1f)
                    .sizeIn(48.dp, 48.dp, 64.dp, 64.dp)
                    .background (
                        color = colors.background,
                        shape = CircleShape
                    )
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (!tutorialInformation.isActive)
                            onAddNewGoalMenuRequested()
                    },
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(colors.surface)
            )
        }

        LaunchedEffect(Unit) {
            goalsSectionViewModel.reloadGoals()
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
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
                        .combinedClickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                            },
                            onLongClick = {
                                menuOpen = true
                                goalIdToDelete = it.id
                            }
                        )
                ) {
                    val bulletPointSize = with(LocalDensity.current) {
                        typography.body.toDp() * 0.8f
                    }

                    Image (
                        painter = painterResource(R.drawable.bulletpoint_foreground),
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
                            .width(20.dp)
                    )

                    Text (
                        text = it.goal,
                        color = colors.textPrimary,
                        fontSize = typography.body * 0.9f,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }

        DropdownMenu (
            modifier = Modifier
                .background (
                    color = colors.primary
                ),
            expanded = menuOpen,
            onDismissRequest = {
                menuOpen = false
            },
        ) {
            DropdownMenuItem (
                modifier = Modifier
                    .background (
                        color = colors.primary,
                    ),
                text = {
                    Text (
                        text = "Delete",
                        color = colors.secondary
                    )
                },
                onClick = {
                    menuOpen = false
                    goalsSectionViewModel.deleteGoal(goalIdToDelete)
                }
            )
        }
    }
}