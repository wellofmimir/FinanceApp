package studio.lemniscate.greeen.homescreen

import androidx.compose.foundation.Image
import studio.lemniscate.greeen.advertisement.AdSectionMiddleBanner
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.dailytipscreen.DailyTipScreenViewModel
import studio.lemniscate.greeen.receiptsscreen.ReceiptSectionsViewModel
import studio.lemniscate.greeen.settingsscreen.SettingsViewModel
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.welldone.WellDoneSection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.sizeIn

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign

import studio.lemniscate.greeen.R


@Composable
fun HomeScreen (
    tutorialInformation: TutorialInformation,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    goalsSectionViewModel: GoalsSectionViewModel,
    dailyTipScreenViewModel: DailyTipScreenViewModel,
    quoteViewModel: QuoteViewModel,
    shopViewModel: ThemeShopViewModel,
    badgesViewModel: BadgesViewModel,
    settingsViewModel: SettingsViewModel,
    onGoalAchieved: () -> Unit,
    onWellDoneSectionDismissed: () -> Unit,
    shopSectionClicked: () -> Unit,
    receiptsSectionClicked: () -> Unit,
    recentlyCompletedGoalsSectionClicked: () -> Unit,
    dailyTipsSectionClicked: () -> Unit) {

    val colors = LocalAppColors.current

    var goalAchieved by remember { mutableStateOf(false) }
    var idGoalAchieved by remember { mutableIntStateOf(0)}

    Column (
        modifier = Modifier
            .background (
                color = colors.primary
            ),
        verticalArrangement = Arrangement.Top
    ) {
        if (goalAchieved) {
            onGoalAchieved()

            WellDoneSection (
                modifier = Modifier
                    .fillMaxHeight(),
                goalsSectionViewModel = goalsSectionViewModel,
                punchCardFilled = false,
                idGoal = idGoalAchieved,
                onFinished = {
                    onWellDoneSectionDismissed()
                    goalAchieved = false
                }
            )

        } else {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                GoalProgressSection (
                    modifier = Modifier
                        .weight(1f),
                    onGoalAchieved = { idGoal ->
                        goalAchieved = true
                        idGoalAchieved = idGoal
                    },
                    goalsSectionViewModel = goalsSectionViewModel,
                    badgesViewModel = badgesViewModel,
                    settingsViewModel = settingsViewModel,
                    tutorialInformation = tutorialInformation
                )

                QuoteSection (
                    modifier = Modifier
                        .weight(1f),
                    quoteViewModel = quoteViewModel,
                    badgesViewModel = badgesViewModel,
                    tutorialInformation = tutorialInformation
                )
            }

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background (
                        color = Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoalsSection (
                    modifier = Modifier
                        .weight(4f),
                    tutorialInformation = tutorialInformation,
                    goalsSectionViewModel = goalsSectionViewModel
                )

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                Row (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background (
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    DailyTipSection (
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight(),
                        dailyTipScreenViewModel = dailyTipScreenViewModel,
                        tutorialInformation = tutorialInformation,
                        dailyTipSectionClicked = {
                            dailyTipsSectionClicked()
                        }
                    )

                    Spacer (
                        modifier = Modifier
                            .width(4.dp)
                    )

                    ShopSection (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        tutorialInformation = tutorialInformation,
                        shopSectionClicked = {
                            shopSectionClicked()
                        }
                    )
                }

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background (
                            color = Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    RecentlyCompletedGoalsSection (
                        tutorialInformation = tutorialInformation,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.25f)
                            .clickable() {
                                if (tutorialInformation.isActive)
                                    return@clickable

                                recentlyCompletedGoalsSectionClicked()
                            },
                        goalsSectionViewModel = goalsSectionViewModel
                    )

                    Spacer (
                        modifier = Modifier
                            .width(4.dp)
                    )
                    
                    SavedReceiptsSection (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.25f)
                            .clickable() {
                                if (tutorialInformation.isActive)
                                    return@clickable

                                receiptsSectionClicked()
                            },
                        receiptSectionsViewModel = receiptSectionsViewModel,
                        tutorialInformation = tutorialInformation,
                    )
                }

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                TokenBanner (
                    modifier = Modifier
                        .weight(1f),
                    goalsSectionViewModel = goalsSectionViewModel,
                    tutorialInformation = tutorialInformation
                )
            }
        }
    }
}