package com.example.financeapp

import com.example.financeapp.Goal
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
import androidx.compose.runtime.remember
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.mutableStateListOf
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.ui.graphics.Color

@Composable
fun RecemtlyCompletedGoalsSection(context: Context = LocalContext.current) {


    val recentlyCompletedGoalsSectionViewModel: RecentlyCompletedGoalsSectionViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {
                val database = FinanceAppDatabase.getInstance(context)
                return RecentlyCompletedGoalsSectionViewModel(database) as T
            }
        }
    )

    Column (
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Pistachio,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = "Recently Completed Goals:",
                fontSize = 22.sp,
                modifier = Modifier
                    .weight(0.1f)
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = Pistachio
                    )
                    .padding(horizontal = 24.dp),
            )

            Box (

            ) {
                Image (
                    painter = painterResource(R.drawable.pluszeichen_foreground),
                    contentDescription = "Punktemenu",
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .size(80.dp)
                        .padding(top = 12.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){

                        }
                )
            }
        }

        val goals = remember { mutableStateListOf<Goal>() }
        //goals.addAll(database.getGoals())

        goals.add(Goal(1, "my awesome goal", 1100.0f, 1))
        goals.add(Goal(2, "example goal #2", 12.0f, 2))
        goals.add(Goal(3, "pay the Loch Ness Monster", 3.50f, 1))
        goals.add(Goal(4, "buy a Toyota Corolla", 19999.0f, 2))
        goals.add(Goal(5, "etf invest", 400.0f, 2))

        val statusCompleted = recentlyCompletedGoalsSectionViewModel.getIDGoalStatus("Completed")
        goals.removeAll{ it.idStatus != statusCompleted.id }

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
                    painter = painterResource(R.drawable.bulletpointfilled_foreground),
                    contentDescription = "BulletpointFilled",
                    modifier = Modifier
                        .background(
                            color = Color.Transparent
                        )
                        .size(24.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Emerald)
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