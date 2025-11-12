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
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.alpha
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.withStyle


@Composable
fun RecentlyCompletedGoalsSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val recentlyCompletedGoalsSectionViewModel: RecentlyCompletedGoalsSectionViewModel = viewModel (
        factory = object : ViewModelProvider.Factory {
            override fun<T : ViewModel> create(modelClass: Class<T>): T {
                val database = FinanceAppDatabase.getInstance(context)
                val repository = GoalRepository(database)
                return RecentlyCompletedGoalsSectionViewModel(repository) as T
            }
        }
    )

    val goals by recentlyCompletedGoalsSectionViewModel.goals.collectAsState()
    recentlyCompletedGoalsSectionViewModel.getCompletedGoals()

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.CURRENT_GOALS) 0.1f else 1.0f)
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "Recently Completed Goals:",
                color = Emerald,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .weight(0.8f)
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = Pistachio
                    )
                    .padding(horizontal = 24.dp),
            )

            Column (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 64.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald
                            )
                        ) {
                            append("${goals.size}\n")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald
                            )
                        ) {
                            append("total")
                        }
                    },
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                    color = Emerald
                )

            }
        }

        goals.take(3).forEach { item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp) //aufgrund von Jetpack Compose Fehler muss zurückgeschoben werden
                    .background(
                        shape = RoundedCornerShape(
                            bottomStart = if (item == goals.last()) 12.dp else 0.dp,
                            bottomEnd = if (item == goals.last()) 12.dp else 0.dp
                        ),
                        color = Color.Transparent
                    )
                    .padding(horizontal = 36.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image (
                    painter = painterResource(R.drawable.bulletpointfilled_foreground),
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
                    text = item.goal,
                    color = Emerald
                )
            }
        }
    }
}