package com.example.financeapp.goalhistoryscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun TotalTokensEarnedSection(modifier: Modifier = Modifier, totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel, tutorialInformation: TutorialInformation) {

    val colors = LocalAppColors.current
    val totalTokensEarned by totalGoalsAchievedSectionViewModel.totalTokensEarned.collectAsState()

    LaunchedEffect(Unit) {
        totalGoalsAchievedSectionViewModel.getTotalTokensEarned()
    }

    Box (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_TOTAL_TOKENS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text = totalTokensEarned.toString(),
                textAlign = TextAlign.Center,
                fontSize = 128.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )

            Text (
                text = "lifetime tokens",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        }
    }
}