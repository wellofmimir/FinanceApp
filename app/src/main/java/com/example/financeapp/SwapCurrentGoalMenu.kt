package com.example.financeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.financeapp.ui.theme.Emerald

@Composable
fun SwapCurrentGoalMenu(expanded: Boolean, onCurrentGoalChanged: (String) -> Unit, onDissmissRequest: () -> Unit, onFinished: () -> Unit, context: Context = LocalContext.current) {

    val swapCurrentGoalMenuViewModel: SwapCurrentGoalMenuViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)

                return SwapCurrentGoalMenuViewModel(repository) as T
            }
        }
    )

    DropdownMenu (
        expanded = expanded,
        onDissmissRequest,
        modifier = Modifier
            .background (
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        containerColor = Color.Transparent
    ) {
        Column (
            modifier = Modifier
                .background (
                    shape = RoundedCornerShape(12.dp),
                    color = Emerald
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text = "Swap current goal",
                color = Color.White,
                fontSize = 24.sp
            )

            Spacer (
                modifier = Modifier
                    .height(8.dp)
            )

            HorizontalDivider (
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = Color.White
            )

            Spacer (
                modifier = Modifier
                    .height(48.dp)
            )

            val goals by swapCurrentGoalMenuViewModel.goals.collectAsState()
            swapCurrentGoalMenuViewModel.getInProgressGoals()

            val currentGoal by swapCurrentGoalMenuViewModel.currentGoal.collectAsState()
            swapCurrentGoalMenuViewModel.getCurrentGoal()
            var newCurrentGoal by remember { mutableStateOf(goals.indexOf(currentGoal)) }

            goals.take(5).forEach { item ->

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            shape = RoundedCornerShape(
                                bottomStart = if (item == goals.last()) 12.dp else 0.dp,
                                bottomEnd = if (item == goals.last()) 12.dp else 0.dp
                            ),
                            color = Emerald
                        )
                        .padding(horizontal = 36.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val painter = painterResource (
                        if (newCurrentGoal == goals.indexOf(item) || item.id == currentGoal?.id) R.drawable.bulletpointpistachiofilled_foreground else R.drawable.bulletpointpistachio_foreground
                    )

                    Image (
                        painter = painter,
                        contentDescription = "BulletpointPistachio",
                        modifier = Modifier
                            .background(
                                color = Color.Transparent
                            )
                            .size(24.dp)
                            .clickable (
                                indication = null,
                                interactionSource = remember {MutableInteractionSource()}
                            ) {
                                newCurrentGoal = goals.indexOf(item)
                                swapCurrentGoalMenuViewModel.setCurrentGoal(item)
                                onCurrentGoalChanged(goals.get(newCurrentGoal).goal)
                            }
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )

                    Text (
                        fontSize = 18.sp,
                        color = Color.White,
                        text = item.goal
                    )
                }
            }
        }
    }

}