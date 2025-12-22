package com.example.financeapp.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun RecentlyCompletedGoalsSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, goalsSectionViewModel: GoalsSectionViewModel, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    val goals by goalsSectionViewModel.completedGoals.collectAsState()

    if (tutorialInformation.isActive) {
        goalsSectionViewModel.getExampleGoals()
    } else {
        goalsSectionViewModel.getCompletedGoals()
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS) 0.1f else 1.0f)
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text (
            text = "Recently Completed:",
            color = colors.textPrimary,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .padding(start = 14.dp, top = 18.dp)
        )

        Spacer (
            modifier = Modifier
                .padding(horizontal = 4.dp)
        )

        goals.take(3).forEach { item ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = RoundedCornerShape(
                            bottomStart = if (item == goals.last()) 12.dp else 0.dp,
                            bottomEnd = if (item == goals.last()) 12.dp else 0.dp
                        ),
                        color = Color.Transparent
                    )
                    .padding(start = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image (
                    painter = painterResource(R.drawable.bulletpointfilled_foreground),
                    contentDescription = "Bulletpoint",
                    modifier = Modifier
                        .background (
                            color = colors.surface
                        )
                        .size(24.dp),
                    colorFilter = ColorFilter.tint(colors.background)
                )

                Spacer (
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )

                Text (
                    fontSize = 18.sp,
                    text = item.goal,
                    color = colors.textPrimary
                )
            }
        }
    }
}