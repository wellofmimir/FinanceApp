package com.example.financeapp

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.compose.foundation.shape.RoundedCornerShape
import android.os.Build
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalFocusManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.advertisement.AdSectionMiddleBanner
import com.example.financeapp.advertisement.InterstitialAdManager
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.goalhistoryscreen.AchievementsSection
import com.example.financeapp.goalhistoryscreen.PunchCardSection
import com.example.financeapp.goalhistoryscreen.TotalGoalsAchievedSection
import com.example.financeapp.goalhistoryscreen.TotalGoalsAchievedSectionViewModel
import com.example.financeapp.goalhistoryscreen.TotalTokensEarnedSection
import com.example.financeapp.header.HeaderSection
import com.example.financeapp.header.HeaderSectionViewModel
import com.example.financeapp.homescreen.GoalprogressSection
import com.example.financeapp.homescreen.GoalsSection
import com.example.financeapp.homescreen.GoalsSectionViewModel
import com.example.financeapp.homescreen.QuoteSection
import com.example.financeapp.homescreen.RecentlyCompletedGoalsSection
import com.example.financeapp.homescreen.SavedReceiptsSection
import com.example.financeapp.homescreen.WellDoneSection
import com.example.financeapp.likedquotes.LikedQuotesSection
import com.example.financeapp.notifications.QuotePollingWorker
import com.example.financeapp.notifications.ReceiptReminderPollingWorker
import com.example.financeapp.receiptsscreen.AverageSpentSection
import com.example.financeapp.receiptsscreen.ExpensesOverviewSection
import com.example.financeapp.receiptsscreen.ReceiptLogSection
import com.example.financeapp.receiptsscreen.ReceiptSectionsViewModel
import com.example.financeapp.receiptsscreen.SinceWhenSection
import com.example.financeapp.receiptsscreen.Timespan
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.repositories.ReceiptRepository
import com.example.financeapp.repositories.UserRepository
import com.example.financeapp.settingsscreen.SettingsSection
import com.example.financeapp.welcomescreen.WelcomeScreen
import java.util.concurrent.TimeUnit
import com.example.financeapp.repositories.AdRepository

enum class Screen (id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3),
    SPLASH(4),
    RECEIPTS(5),

    ABOUT_US(6),

    USER_SETTINGS(id = 7)
}

enum class TutorialStep (id: Int) {

    NONE (0),
    RECENTLY_COMPLETED_GOALS (1),
    CURRENT_GOALS (2),
    CURRENT_GOALS_BUTTON (3),
    CURRENT_GOAL (4),
    QUOTE (5),
    DONE (6),
    SAVED_RECEIPTS (7),
    RECEIPTSSECTION(8)
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
            InterstitialAdManager.instance.initialize(this)
            InterstitialAdManager.instance.loadInterstitial(this)

            var context = LocalContext.current
            scheduleDailyQuoteWorker(context)
            scheduleDailyReminderMeWorker(context)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel("quotes", "Quote", NotificationManager.IMPORTANCE_HIGH))
            manager.createNotificationChannel(NotificationChannel("receipts", "Receipt", NotificationManager.IMPORTANCE_HIGH))
            manager.createNotificationChannel(NotificationChannel("reminders", "Reminders", NotificationManager.IMPORTANCE_HIGH))

            val mainActivityViewModel: MainActivityViewModel = viewModel (
                factory = object: ViewModelProvider.Factory {
                    override fun<T: ViewModel> create(modelClass: Class<T>): T {

                        val database = FinanceAppDatabase.getInstance(context)
                        val repository = UserRepository(database)

                        return MainActivityViewModel(repository) as T
                    }
                }
            )

            val receiptSectionsViewModel: ReceiptSectionsViewModel = viewModel (
                factory = object: ViewModelProvider.Factory {
                    override fun <T: ViewModel> create(modelClass: Class<T>): T {

                        val database = FinanceAppDatabase.getInstance(context)
                        val receiptRepository = ReceiptRepository.getInstance(database)
                        val adRepository = AdRepository.getInstance(database)

                        return ReceiptSectionsViewModel(receiptRepository, adRepository) as T
                    }
                }
            )

            val headerSectionViewModel: HeaderSectionViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        val database = FinanceAppDatabase.getInstance(context)
                        return HeaderSectionViewModel(database) as T
                    }
                }
            )

            val goalSectionViewModel: GoalsSectionViewModel = viewModel (
                factory = object: ViewModelProvider.Factory {
                    override fun<T : ViewModel> create(modelClass: Class<T>): T {

                        val database = FinanceAppDatabase.getInstance(context)
                        val repository = GoalRepository(database)

                        return GoalsSectionViewModel(repository) as T
                    }
                }
            )

            val totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel = viewModel (
                factory = object: ViewModelProvider.Factory {
                    override fun <T: ViewModel> create(modelClass: Class<T>): T {

                        val database = FinanceAppDatabase.Companion.getInstance(context)
                        val repository = GoalRepository(database)
                        return TotalGoalsAchievedSectionViewModel(repository) as T
                    }
                }
            )

            var tutorialInformation by remember { mutableStateOf(value = TutorialInformation(!mainActivityViewModel.isTutorialDone, TutorialStep.NONE))}

            mainActivityViewModel.loadUser()
            val user by mainActivityViewModel.user.collectAsState()

            var sectionIdentifier by remember { mutableStateOf(if (user == "DUMMY") Screen.WELCOME else Screen.SPLASH)}
            var goalAchieved by remember { mutableStateOf(false) }

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
                                TutorialStep.QUOTE -> TutorialStep.RECEIPTSSECTION
                                TutorialStep.RECEIPTSSECTION -> TutorialStep.DONE
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
                if (listOf<Screen>(Screen.HOME, Screen.LIKEDQUOTES, Screen.GOALHISTORY, Screen.RECEIPTS, Screen.ABOUT_US, Screen.USER_SETTINGS).contains(sectionIdentifier)) {

                    if (goalAchieved) {

                    } else {
                        HeaderSection (
                            onNewSectionIdentifier = {
                                sectionIdentifier = it
                            },
                            tutorialInformation = tutorialInformation,
                            headerSectionViewModel = headerSectionViewModel
                        )

                        Spacer (
                            modifier = Modifier
                                .padding(2.dp)
                        )
                    }
                }

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.WELCOME,
                    enter = fadeIn (
                        animationSpec = tween (
                            durationMillis = 1000
                        )
                    )
                ) {
                    WelcomeScreen(
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
                        tutorialInformation = tutorialInformation,
                        receiptSectionsViewModel = receiptSectionsViewModel,
                        goalsSectionViewModel = goalSectionViewModel,
                        onGoalAchieved = {
                            goalAchieved = true
                        },
                        onWellDoneSectionDismissed = {
                            goalAchieved = false
                        }
                    )
                }

                if (sectionIdentifier == Screen.LIKEDQUOTES)
                    LikedQuotesSection(
                        tutorialInformation = tutorialInformation
                    )

                if (sectionIdentifier == Screen.GOALHISTORY)
                    GoalHistorySection (
                        totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel
                    )

                if (sectionIdentifier == Screen.RECEIPTS)
                    ReceiptsSection (
                        receiptSectionsViewModel = receiptSectionsViewModel
                    )

                if (sectionIdentifier == Screen.USER_SETTINGS)
                    SettingsScreen (
                        headerSectionViewModel = headerSectionViewModel,
                        tutorialInformation = tutorialInformation
                    )

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
                                text = "Here, you can track your token progress. Tap the plus sign to add a new goal.\n\nWhen you reach 18 token, you get to treat yourself!",
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
                            .offset(y = -200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column (
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text (
                                text = "This is where your see your recent goals as well as your current ones.",
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
                                text = "This is where you see your progress on your current goal. Here you can swap what you want to be working on.\n\nTap the percentage to update your current goal.",
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
                } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTSSECTION) {
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
                                text = "Have fun exploring the app!\n\nCheck out the Receipts-Section where you can track all you receipts.\n\nCheck the Remind-me button and we'll send you a notification as a reminder.",
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
fun HomeScreen(tutorialInformation: TutorialInformation, receiptSectionsViewModel: ReceiptSectionsViewModel, goalsSectionViewModel: GoalsSectionViewModel, onGoalAchieved: () -> Unit, onWellDoneSectionDismissed: () -> Unit, context: Context = LocalContext.current) {

    var goalAchieved by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .background(
                color = Emerald
            ),
        verticalArrangement = Arrangement.Top
    ) {

        if (goalAchieved) {

            onGoalAchieved()
            WellDoneSection (
                modifier = Modifier
                    .fillMaxHeight(),
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
                GoalprogressSection (
                    modifier = Modifier
                        .weight(1f),
                    onGoalReached = {
                        goalAchieved = true
                    },
                    goalsSectionViewModel = goalsSectionViewModel,
                    tutorialInformation = tutorialInformation
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
                    tutorialInformation = tutorialInformation,
                    goalsSectionViewModel = goalsSectionViewModel
                )

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RecentlyCompletedGoalsSection (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        tutorialInformation = tutorialInformation,
                        goalsSectionViewModel = goalsSectionViewModel
                    )

                    SavedReceiptsSection (
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .fillMaxHeight(),
                        receiptSectionsViewModel = receiptSectionsViewModel,
                        tutorialInformation = tutorialInformation
                    )
                }

                AdSectionMiddleBanner (
                    modifier = Modifier
                        .weight(0.3f),
                    tutorialInformation = tutorialInformation
                )
            }
        }
    }
}

@Composable
fun GoalHistorySection(totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel) {

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
                .fillMaxHeight()
        )

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            TotalGoalsAchievedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel
            )

            TotalTokensEarnedSection (
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel
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
            .fillMaxHeight(0.75f)
    )

    Spacer (
        modifier = Modifier
            .padding(2.dp)
    )

    AdSectionLargeBanner (
        tutorialInformation = TutorialInformation (
            isActive = false,
            tutorialStep = TutorialStep.NONE
        )
    )
}

@Composable
fun ReceiptsSection(receiptSectionsViewModel: ReceiptSectionsViewModel, context: Context = LocalContext.current) {

    var timespan by remember { mutableStateOf(Timespan.THIS_MONTH) }
    val activity = context as? Activity
    var receiptAdded by remember { mutableStateOf(false) }

    LaunchedEffect(receiptAdded) {
        activity?.let {
            if (receiptAdded) {
                 if (!receiptSectionsViewModel.interstitialAdAfterReceiptSeen()) {
                    InterstitialAdManager.instance.showInterstitial(
                        activity = it,
                        onAdClosed = {
                            receiptAdded = false
                            receiptSectionsViewModel.setInterstitialAdAfterReceiptSeen()
                        },
                        onAdFailed = {
                        }
                    )
                }
            }
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SinceWhenSection (
            onCurrentMonth = {
                timespan = it
            },
            receiptSectionsViewModel = receiptSectionsViewModel
        )

        Row (
            modifier = Modifier,
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            AverageSpentSection (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                timespan = timespan,
                receiptAdded = {
                    receiptAdded = true
                },
                onDismissRequest = {

                },
                receiptSectionsViewModel = receiptSectionsViewModel
            )

            ExpensesOverviewSection (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                timespan,
                receiptSectionsViewModel
            )
        }

        ReceiptLogSection (
            modifier = Modifier
                .fillMaxHeight(0.8f),
            timespan = timespan,
            receiptSectionsViewModel = receiptSectionsViewModel,
        )

        AdSectionLargeBanner (
            tutorialInformation = TutorialInformation(
                isActive = false,
                tutorialStep = TutorialStep.NONE
            )
        )
    }
}
@Composable
fun SettingsScreen(headerSectionViewModel: HeaderSectionViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    SettingsSection (
        headerSectionViewModel = headerSectionViewModel
    )

    Spacer (
        modifier = Modifier
            .height(4.dp)
    )

    AdSectionMiddleBanner(tutorialInformation = tutorialInformation)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestNotificationPermission() {

    if (Build.VERSION.SDK_INT < 33)
        return

    val permissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
    }
}

fun scheduleDailyReminderMeWorker(context: Context) {

    val now = Calendar.getInstance()
    val nextReminder = Calendar.getInstance().apply {

        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (before(now))
            add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = nextReminder.timeInMillis - now.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<ReceiptReminderPollingWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork (
        "dailyReminderWorker",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}

fun scheduleDailyQuoteWorker(context: Context) {

    val now = Calendar.getInstance()
    val next22 = Calendar.getInstance().apply {

        set(Calendar.HOUR_OF_DAY, 17)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (before(now))
            add(Calendar.DAY_OF_MONTH, 1)
    }

    val initialDelay = next22.timeInMillis - now.timeInMillis

    val workRequest = PeriodicWorkRequestBuilder<QuotePollingWorker>(1, TimeUnit.DAYS)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork (
        "dailyQuoteWorker",
        ExistingPeriodicWorkPolicy.REPLACE,
        workRequest
    )
}

