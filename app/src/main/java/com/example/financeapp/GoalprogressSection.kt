package com.example.financeapp

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun GoalprogressSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    val goalprogressSectionViewModel: GoalprogressSectionViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)

                return GoalprogressSectionViewModel(repository) as T
            }
        }
    )

    val currentGoal by goalprogressSectionViewModel.currentGoal.collectAsState()
    goalprogressSectionViewModel.getCurrentGoal()
    var currentGoalText by remember { mutableStateOf("") }
    var currentGoalAmount by remember { mutableStateOf("") }
    var currentGoalAmountAlreadySaved by remember { mutableStateOf("14000.50") }

    LaunchedEffect(currentGoal) {
        currentGoal?.let {
            currentGoalText = it.goal
            currentGoalAmount = it.amount.toString()
        }
    }

    Column (
        modifier = modifier
            .aspectRatio(1f),
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
                .padding(top = 8.dp, bottom = 0.dp, start = 8.dp, end = 8.dp)
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
                        .fillMaxWidth()
                        .padding(bottom = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Current goal:",
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Text(
                        text = currentGoalText,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp, start = 2.dp, end = 2.dp)
                    )
                }
            }

            var expanded by remember {mutableStateOf(false)}

            Image (
                painter = painterResource(com.example.financeapp.R.drawable.doppelpfeilerunter_foreground),
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

            SwapCurrentGoalMenu (
                expanded = expanded,
                onCurrentGoalChanged = {newText -> currentGoalText = newText},
                onDissmissRequest = { expanded = false},
                onFinished = { expanded = false }
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .background(
                    color = Pistachio
                )
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "Amount: $currentGoalAmount\nSaved: $currentGoalAmountAlreadySaved",
                fontSize = 16.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
                .background(
                    color = Pistachio
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "90%",
                fontSize = 64.sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Bold
            )
        }
    }
}