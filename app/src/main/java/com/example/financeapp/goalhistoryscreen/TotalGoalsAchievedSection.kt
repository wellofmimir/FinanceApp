package com.example.financeapp.goalhistoryscreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun TotalGoalsAchievedSection(modifier: Modifier = Modifier, totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    val goals by totalGoalsAchievedSectionViewModel.goals.collectAsState()
    totalGoalsAchievedSectionViewModel.getCompletedGoals()

    Box (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_TOTAL_GOALS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textSize = when (goals.size.toString().length) {
                1 -> 128.sp
                2 -> 128.sp
                3 -> 92.sp
                4 -> 64.sp
                5 -> 32.sp
                6 -> 16.sp
                else -> 16.sp
            }

            if (goals.size.toString().length < 2) {
                Spacer (
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Text (
                text = "${goals.size}",
                textAlign = TextAlign.Center,
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Spacer (
                modifier = Modifier
                    .weight(1f)
            )

            Text (
                text = "total goals achieved",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
    }
}