package com.example.financeapp.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.Emerald

@Composable
fun GoalprogressSection(modifier: Modifier = Modifier, onGoalReached: () -> Unit, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val goalprogressSectionViewModel: GoalprogressSectionViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val repository = GoalRepository(database)

                return GoalprogressSectionViewModel(repository) as T
            }
        }
    )

    val currentGoalPercentage by goalprogressSectionViewModel.percentageOfCurrentGoal.collectAsState()
    val currentGoal by goalprogressSectionViewModel.currentGoal.collectAsState()
    goalprogressSectionViewModel.getCurrentGoal()

    var currentGoalText by remember { mutableStateOf("") }

    LaunchedEffect(currentGoal) {

        if (currentGoal == null)
            currentGoalText = ""

        currentGoal?.let {
            currentGoalText = it.goal
            goalprogressSectionViewModel.calculateCurrentGoalPercentage()
        }
    }

    LaunchedEffect(currentGoalText) {

        if (currentGoal == null)
            currentGoalText = ""

        currentGoal?.let {
            currentGoalText = it.goal
            goalprogressSectionViewModel.calculateCurrentGoalPercentage()
        }
    }


    Column (
        modifier = modifier
            .aspectRatio(1f)
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.CURRENT_GOAL) 0.1f else 1.0f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = Pistachio
                )
                .padding(top = 8.dp, end = 8.dp)
        ) {
            Box (
                modifier = Modifier
                    .weight(1f)
                    .background(
                        shape = RoundedCornerShape(12.dp),
                        color = Pistachio
                    ),
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text (
                        text = "Current goal:\n",
                        color = Emerald,
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
                        color = Emerald,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, start = 2.dp, end = 2.dp)
                    )
                }
            }

            var expanded by remember {mutableStateOf(false)}

            Image (
                painter = painterResource(R.drawable.doppelpfeilerunter_foreground),
                contentDescription = "Doppelpfeile",
                modifier = Modifier
                    .height(64.dp)
                    .aspectRatio(1f)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expanded = true
                    }
            )

            SwapCurrentGoalMenu(
                expanded = expanded,
                onCurrentGoalChanged = { newText -> currentGoalText = newText },
                onDissmissRequest = { expanded = false }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(
                    color = Pistachio,
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            var expanded by remember { mutableStateOf(false) }

            if (currentGoal != null) {
                Text (
                    text = currentGoalPercentage.toString() + "%",
                    fontSize = 64.sp,
                    textAlign = TextAlign.Right,
                    fontWeight = FontWeight.Bold,
                    color = Emerald,
                    modifier = modifier
                        .padding(end = 8.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember {MutableInteractionSource()}
                        ) {
                            expanded = true
                        }
                )
            }

            EditGoalMenu (
                expanded,
                goal = currentGoal,
                onDismissRequest = { expanded = false },
                onNewAmount = { amount ->

                    val newAmount = amount.toFloat()
                    val updatedCurrentGoal = currentGoal
                    updatedCurrentGoal!!.amount = newAmount

                    goalprogressSectionViewModel.updateGoal(updatedCurrentGoal)
                },
                onSaved = { savedAmount ->

                    val newSavedAmount = savedAmount.toFloat()
                    val updatedCurrentGoal = currentGoal
                    updatedCurrentGoal!!.saved = newSavedAmount

                    goalprogressSectionViewModel.updateGoal(updatedCurrentGoal)
                    goalprogressSectionViewModel.calculateCurrentGoalPercentage()

                    val goalPercentage = goalprogressSectionViewModel.getCurrentGoalPercentage()

                    if (goalPercentage >= 100) {
                        goalprogressSectionViewModel.setGoalCompleted(updatedCurrentGoal)
                        goalprogressSectionViewModel.addToTotalTokensEarned(updatedCurrentGoal.tokenCount)
                        onGoalReached()
                    }
                },
                currentGoalText
            )
        }
    }
}