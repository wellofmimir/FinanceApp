package studio.lemniscate.greeen.goalhistoryscreen


import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import studio.lemniscate.greeen.ui.theme.LocalAppTypography


@Composable
fun TotalTokensEarnedSection (
    modifier: Modifier = Modifier,
    totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val totalTokensEarned by totalGoalsAchievedSectionViewModel.totalTokensEarned.collectAsState()

    LaunchedEffect(Unit) {
        totalGoalsAchievedSectionViewModel.getTotalTokensEarned()
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_TOTAL_TOKENS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fontSizeMultiplicator = when (totalTokensEarned.toString().length) {
            1 -> 5f
            2 -> 5f
            3 -> 4f
            4 -> 3f
            5 -> 2.5f
            6 -> 1f
            else -> 0.75f
        }

        Text (
            text = buildAnnotatedString {
                withStyle (
                    SpanStyle (
                        fontSize = typography.subtitle * fontSizeMultiplicator,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                ) {
                    append(totalTokensEarned.toString())
                }

                append("\n")

                withStyle (
                    SpanStyle (
                        fontSize = typography.medium,
                        color = colors.primary
                    )
                ) {
                    append("lifetime tokens")
                }
            },
            textAlign = TextAlign.Center,
        )
    }
}