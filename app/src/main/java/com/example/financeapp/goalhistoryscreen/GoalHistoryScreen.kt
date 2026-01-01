package com.example.financeapp.goalhistoryscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.TutorialInformation
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.homescreen.AchievementsSectionViewModel

@Composable
fun GoalHistoryScreen(totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel, achievementsSectionViewModel: AchievementsSectionViewModel, tutorialInformation: TutorialInformation) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        PunchCardSection (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            tutorialInformation = tutorialInformation
        )

        Column (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var randomBoolean by remember { mutableStateOf( kotlin.random.Random.nextBoolean()) }

            if (randomBoolean)
                TotalGoalsAchievedSection (
                    modifier = Modifier
                        .weight(1f)
                        .clickable() {
                            randomBoolean = !randomBoolean
                        },
                    totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                    tutorialInformation = tutorialInformation
                )
            else
                TotalTokensEarnedSection (
                    modifier = Modifier
                        .weight(1f)
                        .clickable() {
                            randomBoolean = !randomBoolean
                        },
                    totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                    tutorialInformation = tutorialInformation
                )

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            RandomMemoryPictureSection (
                modifier = Modifier
                    .weight(1f),
                achievementsSectionViewModel = achievementsSectionViewModel
            )
        }
    }

    Spacer (
        modifier = Modifier
            .padding(2.dp)
    )

    AchievementsSection (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f),
        achievementsSectionViewModel = achievementsSectionViewModel,
        tutorialInformation = tutorialInformation
    )

    Spacer (
        modifier = Modifier
            .padding(2.dp)
    )

    AdSectionLargeBanner (
        tutorialInformation = tutorialInformation
    )
}