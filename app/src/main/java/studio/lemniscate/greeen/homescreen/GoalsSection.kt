package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import android.content.Context
import android.widget.Toast

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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

import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoalsSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    goalsSectionViewModel: GoalsSectionViewModel,
    context: Context = LocalContext.current)
{
    val colors = LocalAppColors.current

    val goals by goalsSectionViewModel.goals.collectAsState()
    var newGoalEntered by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var menuOpen by remember { mutableStateOf(false) }
    var goalIdToDelete by remember { mutableStateOf(0) }

    AddNewGoalMenu (
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
        onFinished = {
            expanded = false
            newGoalEntered = true
            goalsSectionViewModel.reloadGoals()
        },
        goalsSectionViewModel = goalsSectionViewModel
    )

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_CURRENT_GOALS) 0.1f else 1.0f)
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
            horizontalArrangement = Arrangement.Center
        ) {
            Text (
                text = "Stuff you're working on:",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = colors.surface
                    )
                    .padding(start = 18.dp, end = 0.dp, bottom = 0.dp)
                    .weight(2f)
            )

            Box (
                modifier = Modifier
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
                    painter = painterResource(R.drawable.pluszeichen_standard_pistachio_foreground),
                    contentDescription = "Pluszeichen",
                    modifier = Modifier
                        .size(48.dp)
                        .background (
                            color = colors.background,
                            shape = CircleShape
                        )
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (tutorialInformation.isActive)
                                return@clickable

                            expanded = true
                        },
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(colors.surface)
                )
            }
        }

        LaunchedEffect(Unit) {
            goalsSectionViewModel.reloadGoals()
        }

        if (newGoalEntered) {

            val listState = rememberLazyListState()

            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp)
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
                        Image (
                            painter = painterResource(R.drawable.bulletpoint_foreground),
                            contentDescription = "Bulletpoint",
                            modifier = Modifier
                                .background (
                                    color = colors.surface
                                )
                                .size(24.dp),
                            colorFilter = ColorFilter.tint(colors.background)
                        )

                        Spacer (
                            modifier = Modifier
                                .width(24.dp)
                        )

                        Text (
                            text = it.goal,
                            color = colors.textPrimary,
                            fontSize = 18.sp,
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
}