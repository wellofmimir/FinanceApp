package com.example.financeapp.goalhistoryscreen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.homescreen.AchievementsSectionViewModel
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun AchievementsSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    val achievementsSectionViewModel: AchievementsSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val repository = GoalRepository(database)

                return AchievementsSectionViewModel(repository) as T
            }
        }
    )

    val goals by achievementsSectionViewModel.goals.collectAsState()
    achievementsSectionViewModel.getCompletedGoals()

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_ACHIEVEMENTS) 0.1f else 1.0f)
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
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "What You've Achieved:",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = colors.surface
                    )
                    .padding(start = 24.dp, top = 24.dp, end = 0.dp, bottom = 24.dp)
            )
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            state = listState
        ) {
            items(goals.take(goals.size)) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text (
                        text = it.goal,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 40.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
                            .weight(1f)
                    )

                    Text (
                        text = it.dateWhenFinished,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
                            .weight(1f)
                    )
                }
            }
        }

    }
}