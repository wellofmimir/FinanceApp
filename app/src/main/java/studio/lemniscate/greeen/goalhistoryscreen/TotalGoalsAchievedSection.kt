package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import studio.lemniscate.greeen.ui.theme.LocalAppTypography


@Composable
fun TotalGoalsAchievedSection (
    modifier: Modifier = Modifier,
    totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val goals by totalGoalsAchievedSectionViewModel.goals.collectAsState()

    LaunchedEffect(Unit) {
        totalGoalsAchievedSectionViewModel.getCompletedGoals()
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_TOTAL_GOALS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fontSizeMultiplicator = when (goals.size) {
            in 0..9 -> 5f
            in 10..99 -> 5f
            in 100..999 -> 4f
            in 1000..9999 -> 3f
            in 10000..99999 ->1.5f
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
                    append(if (goals.isEmpty()) "0" else goals.size.toString())
                }

                append("\n")

                withStyle (
                    SpanStyle (
                        fontSize = typography.medium ,
                        color = colors.primary
                    )
                ) {
                    append("total goals achieved")
                }
            },
            textAlign = TextAlign.Center,
        )
    }
}