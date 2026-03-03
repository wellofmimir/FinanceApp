package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.advertisement.AdSectionLargeBanner
import studio.lemniscate.greeen.homescreen.AchievementsSectionViewModel
import studio.lemniscate.greeen.homescreen.GoalsSectionViewModel
import studio.lemniscate.greeen.welldone.WellDoneSection
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun GoalHistoryScreen (
    goalsSectionViewModel: GoalsSectionViewModel,
    totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel,
    punchCardSectionViewModel: PunchCardSectionViewModel,
    achievementsSectionViewModel: AchievementsSectionViewModel,
    shopViewModel: ThemeShopViewModel,
    tutorialInformation: TutorialInformation,
    onPunchCardFilled: () -> Unit,
    onWellDoneSectionDismissed: () -> Unit) {

    var punchCardFilled by remember { mutableStateOf(false) }
    var switchSections by remember { mutableStateOf(true) }
    val adremoverActive by shopViewModel.adRemoverPurchased.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        totalGoalsAchievedSectionViewModel.getCompletedGoals()
        totalGoalsAchievedSectionViewModel.getTotalTokensEarned()
    }

    if (punchCardFilled) {

        //Jede ausgefüllte PunchCard soll auch einfach als erfülltes Goal behandelt werden.
        //Deshalb nuss ein neues Ziel in die Datenbank gesetzt werden

        goalsSectionViewModel.insertGoal("Filled out the punch card", 0.0f, "PunchCard", 15)
        val newestGoalId = goalsSectionViewModel.getNewestGoalId()
        onPunchCardFilled()

        WellDoneSection (
            modifier = Modifier
                .fillMaxSize(),
            goalsSectionViewModel = goalsSectionViewModel,
            punchCardFilled = true,
            idGoal = newestGoalId,
            onFinished = {
                onWellDoneSectionDismissed()
                punchCardFilled = false
            }
        )

        return
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                tutorialInformation = tutorialInformation,
                punchCardSectionViewModel = punchCardSectionViewModel,
                onPunchCardFilled = {
                    punchCardFilled = true
                }
            )

            Column (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (switchSections)
                    TotalGoalsAchievedSection (
                        modifier = Modifier
                            .weight(1f)
                            .clickable() {
                                if (tutorialInformation.isActive)
                                    return@clickable

                                switchSections = !switchSections
                            },
                        totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                        tutorialInformation = tutorialInformation
                    )
                else
                    TotalTokensEarnedSection (
                        modifier = Modifier
                            .weight(1f)
                            .clickable() {
                                if (tutorialInformation.isActive)
                                    return@clickable

                                switchSections = !switchSections
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
                    tutorialInformation = tutorialInformation,
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
                .fillMaxHeight()
                .weight(3f),
            achievementsSectionViewModel = achievementsSectionViewModel,
            tutorialInformation = tutorialInformation
        )

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        AdSectionLargeBanner (
            modifier = Modifier
                .weight(1f),
            suppressAd = adremoverActive,
            tutorialInformation = tutorialInformation
        )
    }




}