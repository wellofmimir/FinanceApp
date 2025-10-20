package com.example.financeapp

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun GoalsSection(context: Context = LocalContext.current) {

    val goalSectionViewModel: GoalsSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)

                return GoalsSectionViewModel(repository) as T
            }
        }
    )

    val goals by goalSectionViewModel.goals.collectAsState()
    var newGoalEntered by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.15f)
            .background(
                color = Pistachio,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text (
                text = "Stuff you're working on:",
                fontSize = 22.sp,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = Pistachio
                    )
                    .padding(start = 0.dp, top = 26.dp, end = 0.dp, bottom = 0.dp)
            )

            Image (
                painter = painterResource(R.drawable.pluszeichen_foreground),
                contentDescription = "Pluszeichen",
                modifier = Modifier
                    .size(64.dp)
                    .offset(y = 8.dp, x = 50.dp)
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ){
                        expanded = true
                        visible = true
                    },
                alignment = Alignment.TopEnd
            )

            this@Row.AnimatedVisibility (
                visible = visible,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                AddNewGoalMenu (
                    expanded,
                    onDismissRequest = { expanded = false },
                    onFinished = {
                        expanded  = false
                        newGoalEntered = true
                        goalSectionViewModel.reloadGoals()
                    }
                )
            }
        }

        LaunchedEffect(Unit) {
            goalSectionViewModel.reloadGoals()

            if (goalSectionViewModel.goals.value.isEmpty())
                goalSectionViewModel.insertExampleGoals()
        }

        if (newGoalEntered) {

            goals.take(5).forEach { item ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(
                                bottomStart = if (item == goals.last()) 12.dp else 0.dp,
                                bottomEnd = if (item == goals.last()) 12.dp else 0.dp
                            ),
                            color = Pistachio
                        )
                        .padding(horizontal = 36.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image (
                        painter = painterResource(R.drawable.bulletpoint_foreground),
                        contentDescription = "Bulletpoint",
                        modifier = Modifier
                            .background(
                                color = Pistachio
                            )
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(Emerald)
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    Text (
                        fontSize = 18.sp,
                        text = item.goal
                    )
                }
            }
        }
    }
}