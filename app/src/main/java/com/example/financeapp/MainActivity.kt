package com.example.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.financeapp.ui.theme.Emerald
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.delay
import androidx.compose.material3.Text
import androidx.compose.animation.core.tween
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment

enum class Screen (id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3),
    SPLASH(4),

    RECEIPTS(5),

    ABOUT_US(6)
}

enum class TutorialStep (id: Int) {

    NONE (0),
    RECENTLY_COMPLETED_GOALS (1),
    CURRENT_GOALS (2),
    CURRENT_GOALS_BUTTON (3),
    CURRENT_GOAL (4),
    QUOTE (5),

    DONE (6)
}

data class TutorialInformation (

    var isActive: Boolean,
    var tutorialStep: TutorialStep
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MobileAds.initialize(this)
            var context = LocalContext.current

            val mainActivityViewModel: MainActivityViewModel = viewModel (
                factory = object: ViewModelProvider.Factory {
                    override fun<T: ViewModel> create(modelClass: Class<T>): T {

                        val database = FinanceAppDatabase.getInstance(context)
                        val repository = UserRepository(database)

                        return MainActivityViewModel(repository) as T
                    }
                }
            )

            var tutorialInformation by remember { mutableStateOf(value = TutorialInformation(!mainActivityViewModel.isTutorialDone, TutorialStep.NONE))}

            mainActivityViewModel.loadUser()
            val user by mainActivityViewModel.user.collectAsState()

            var sectionIdentifier by remember { mutableStateOf(if (user == "DUMMY") Screen.WELCOME else Screen.SPLASH)}

            LaunchedEffect(user) {

                if (sectionIdentifier == Screen.SPLASH && (!user.isEmpty() && user != "DUMMY"))
                    delay(2000)

                sectionIdentifier = when (user) {
                    "DUMMY" -> Screen.WELCOME
                    "" -> Screen.WELCOME
                    else -> Screen.HOME
                }
            }

            Column (
                modifier = Modifier
                    .background(Emerald)
                    .fillMaxSize()
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        tutorialInformation = tutorialInformation.copy (
                            isActive = tutorialInformation.isActive,
                            tutorialStep =  when (tutorialInformation.tutorialStep) {
                                TutorialStep.NONE -> TutorialStep.RECENTLY_COMPLETED_GOALS
                                TutorialStep.RECENTLY_COMPLETED_GOALS -> TutorialStep.CURRENT_GOALS
                                TutorialStep.CURRENT_GOALS -> TutorialStep.CURRENT_GOAL
                                TutorialStep.CURRENT_GOAL -> TutorialStep.QUOTE
                                TutorialStep.QUOTE -> TutorialStep.DONE
                                else -> TutorialStep.NONE
                            }
                        )

                        if (tutorialInformation.tutorialStep == TutorialStep.DONE) {
                            tutorialInformation = tutorialInformation.copy(
                                isActive = false,
                                tutorialStep = TutorialStep.NONE
                            )

                            mainActivityViewModel.updateTutorialDoneStatus(true)
                        }
                    }
            ) {
                // Header nur, wenn nicht Welcome
                if (listOf<Screen>(Screen.HOME, Screen.LIKEDQUOTES, Screen.GOALHISTORY, Screen.RECEIPTS, Screen.ABOUT_US).contains(sectionIdentifier)) {
                    HeaderSection(onNewSectionIdentifier = {
                            sectionIdentifier = it
                        },
                        tutorialInformation = tutorialInformation
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(2.dp)
                    )
                }

                // Aktueller Screen

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.WELCOME,
                    enter = fadeIn (
                        animationSpec = tween (
                            durationMillis = 1000
                        )
                    )
                ) {
                    WelcomeScreen (
                        onFinished = {
                            mainActivityViewModel.loadUser()
                        },
                        false
                    )
                }

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.HOME,
                    enter = fadeIn (
                        animationSpec = tween (
                            durationMillis = 1000
                        )
                    )
                ) {
                    HomeScreen (
                        tutorialInformation = tutorialInformation
                    )
                }

                if (sectionIdentifier == Screen.LIKEDQUOTES)
                    LikedQuotesSection (
                        tutorialInformation = tutorialInformation
                    )

                if (sectionIdentifier == Screen.GOALHISTORY)
                    GoalHistorySection()

                if (sectionIdentifier == Screen.RECEIPTS)
                    ReceiptsSection()

                if (sectionIdentifier == Screen.ABOUT_US)
                    AboutUsSection()

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.SPLASH,
                    enter = fadeIn (
                        animationSpec = tween (
                            durationMillis = 1000
                        )
                    )
                ) {
                    WelcomeScreen (
                        onFinished = {
                            mainActivityViewModel.loadUser()
                        },
                        true
                    )
                }
            }

            if (sectionIdentifier == Screen.HOME) {

                if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.NONE) {
                    Box (
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "Welcome to your new goal tracking space.",
                                fontSize = 24.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECENTLY_COMPLETED_GOALS) {

                    Box (
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "Here, you can track your token progress. Tap to see your punch card, and tracking on goals you've completed.\n\nWhen you reach fifteen goals completed, you get to treat yourself!",
                                fontSize = 24.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.CURRENT_GOALS) {

                    Box (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding (
                                top = 400.dp
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "This is where your see your current objectives, besides your targeted goal.",
                                fontSize = 24.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.CURRENT_GOAL) {

                    Box (
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "This is where you see your progress on your current goal. Here you can swap what you want to be working on.",
                                fontSize = 24.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.QUOTE) {

                    Box (
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "Everyone needs words of motivation. Here you can view inspirational quotes, and like them to save for later.",
                                fontSize = 24.sp,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(tutorialInformation: TutorialInformation) {

    Column (
        modifier = Modifier
            .background(
                color = Emerald
            ),
        verticalArrangement = Arrangement.Top
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            GoalprogressSection (
                modifier = Modifier
                    .weight(1f),
                tutorialInformation = tutorialInformation
            )

            Spacer (
                modifier = Modifier
                    .padding(horizontal = 2.dp)
            )

            QuoteSection (
                modifier = Modifier
                    .weight(1f),
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
                .fillMaxHeight()
                .background (
                    color = Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GoalsSection (
                modifier = Modifier
                    .weight(1f),
                tutorialInformation = tutorialInformation
            )

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            RecentlyCompletedGoalsSection (
                modifier = Modifier
                    .weight(0.7f),
                tutorialInformation = tutorialInformation
            )

            AdSectionMiddleBanner (
                modifier = Modifier
                    .weight(0.3f),
                tutorialInformation = tutorialInformation
            )
        }
    }
}

@Composable
fun GoalHistorySection() {

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            PunchCardSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                TotalGoalsAchievedSection (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )

                TotalTokensEarnedSection (
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }

        Spacer (
            modifier = Modifier
                .padding(1.dp)
        )

        AchievementsSection (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
        )

        Spacer (
            modifier = Modifier
                .padding(1.dp)
        )

        AdSectionLargeBanner (
            tutorialInformation = TutorialInformation (
                isActive = false,
                tutorialStep = TutorialStep.NONE
            )
        )
}

@Composable
fun ReceiptsSection() {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        PunchCardSection (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            TotalGoalsAchievedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            TotalTokensEarnedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }

    Spacer (
        modifier = Modifier
            .padding(1.dp)
    )

    AchievementsSection (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
    )

    Spacer (
        modifier = Modifier
            .padding(1.dp)
    )

    AdSectionLargeBanner (
        tutorialInformation = TutorialInformation (
            isActive = false,
            tutorialStep = TutorialStep.NONE
        )
    )
}

@Composable
fun AboutUsSection() {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        PunchCardSection (
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            TotalGoalsAchievedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            TotalTokensEarnedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }

    Spacer (
        modifier = Modifier
            .padding(1.dp)
    )

    AchievementsSection (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
    )

    Spacer (
        modifier = Modifier
            .padding(1.dp)
    )

    AdSectionLargeBanner (
        tutorialInformation = TutorialInformation (
            isActive = false,
            tutorialStep = TutorialStep.NONE
        )
    )
}