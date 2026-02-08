package studio.lemniscate.greeen.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.settingsscreen.SettingsViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppColors

@Composable
fun GoalProgressSection (
    modifier: Modifier = Modifier,
    onGoalAchieved: (idGoal: Int) -> Unit,
    goalsSectionViewModel: GoalsSectionViewModel,
    badgesViewModel: BadgesViewModel,
    settingsViewModel: SettingsViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    val currentGoalPercentage by goalsSectionViewModel.percentageOfCurrentGoal.collectAsState()
    val currentGoal by goalsSectionViewModel.currentGoal.collectAsState()

    LaunchedEffect(Unit) {
        goalsSectionViewModel.getCurrentGoal()
    }

    var currentGoalText by remember { mutableStateOf(currentGoal?.goal.toString()) }

    LaunchedEffect(currentGoal) {
        if (currentGoal == null)
            currentGoalText = ""

        currentGoal?.let {
            currentGoalText = it.goal
            goalsSectionViewModel.calculateCurrentGoalPercentage()
        }
    }

    LaunchedEffect(currentGoalText) {
        if (currentGoal == null)
            currentGoalText = ""

        currentGoal?.let {
            currentGoalText = it.goal
            goalsSectionViewModel.calculateCurrentGoalPercentage()
        }
    }

    var expandedEditMenu by remember {mutableStateOf(false)}
    var expanded by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_CURRENT_GOAL) 0.1f else 1.0f)
            .aspectRatio(1f)
            .fillMaxSize()
            .background (
                shape = RoundedCornerShape(12.dp),
                color = colors.secondary
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box (
                modifier = Modifier
                    .weight(2f)
                    .background (
                        shape = RoundedCornerShape(12.dp),
                        color = colors.surface
                    ),
            ) {
                EditGoalMenu (
                    expandedEditMenu,
                    goal = currentGoal,
                    onDismissRequest = { expandedEditMenu = false },
                    onNewAmount = { amount ->

                        val newAmount = amount.toFloat()
                        val updatedCurrentGoal = currentGoal
                        updatedCurrentGoal!!.amount = newAmount

                        goalsSectionViewModel.updateGoal(updatedCurrentGoal)
                    },
                    onSaved = { savedAmount ->

                        val newSavedAmount = savedAmount.toFloat()
                        val updatedCurrentGoal = currentGoal
                        updatedCurrentGoal!!.saved = newSavedAmount

                        goalsSectionViewModel.updateGoal(updatedCurrentGoal)
                        goalsSectionViewModel.calculateCurrentGoalPercentage()

                        val goalPercentage = goalsSectionViewModel.getCurrentGoalPercentage()

                        if (goalPercentage >= 100) {
                            goalsSectionViewModel.setGoalCompleted(updatedCurrentGoal)
                            goalsSectionViewModel.addToTotalTokensEarned(updatedCurrentGoal.tokenCount)
                            onGoalAchieved(updatedCurrentGoal.id)

                            badgesViewModel.checkBadge(BadgeIdentifier.FIRST_GOAL)
                        }
                    },
                    currentGoalText,
                    currency = settingsViewModel.currency.collectAsState().value
                )

                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text (
                        text = "Your goal:",
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )

                    Text (
                        text = currentGoalText,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 2.dp, end = 2.dp)
                    )
                }
            }

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
                            if (tutorialInformation.isActive)
                                return@clickable

                            expanded = true
                        },
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(colors.surface)
                )
            }

            SwapCurrentGoalMenu (
                expanded = expanded,
                onDismissRequest = { expanded = false },
                goalsSectionViewModel = goalsSectionViewModel
            )
        }

        Box (
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (currentGoal != null) {
                Text (
                    text = "${currentGoalPercentage.toInt()}%",
                    fontSize = 64.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable (
                            indication = null,
                            interactionSource = remember {MutableInteractionSource()}
                        ) {
                            if (tutorialInformation.isActive)
                                return@clickable

                            expandedEditMenu = true
                        }
                )
            }
        }

    }
}