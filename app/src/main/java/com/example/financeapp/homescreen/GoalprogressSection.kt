package com.example.financeapp.homescreen

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
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun GoalprogressSection(modifier: Modifier = Modifier, onGoalReached: () -> Unit, goalsSectionViewModel: GoalsSectionViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    val currentGoalPercentage by goalsSectionViewModel.percentageOfCurrentGoal.collectAsState()
    val currentGoal by goalsSectionViewModel.currentGoal.collectAsState()
    goalsSectionViewModel.getCurrentGoal()

    var currentGoalText by remember { mutableStateOf("") }

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

    Column (
        modifier = modifier
            .aspectRatio(1f)
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_CURRENT_GOAL) 0.1f else 1.0f),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Top
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background (
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = colors.surface
                )
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Box (
                modifier = Modifier
                    .weight(1f)
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
                            onGoalReached()
                        }
                    },
                    currentGoalText
                )

                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text (
                        text = "Current goal:\n",
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    )

                    Text (
                        text = currentGoalText,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.textPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, start = 2.dp, end = 2.dp)
                    )
                }
            }

            var expanded by remember {mutableStateOf(false)}

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
                            expanded = true
                        },
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    colorFilter = ColorFilter.tint(colors.surface)
                )
            }

            SwapCurrentGoalMenu (
                expanded = expanded,
                onCurrentGoalChanged = { newText -> currentGoalText = newText },
                onDissmissRequest = { expanded = false },
                goalsSectionViewModel = goalsSectionViewModel
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentGoal != null) {
                Text (
                    text = currentGoalPercentage.toString() + "%",
                    fontSize = 64.sp,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    modifier = modifier
                        .padding(end = 8.dp, bottom = 8.dp)
                        .clickable(
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