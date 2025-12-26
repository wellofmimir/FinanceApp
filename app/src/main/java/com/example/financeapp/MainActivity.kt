package com.example.financeapp
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.advertisement.AdSectionMiddleBanner
import com.example.financeapp.advertisement.InterstitialAdManager
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.billingmanager.BillingManager
import com.example.financeapp.goalhistoryscreen.AchievementsSection
import com.example.financeapp.goalhistoryscreen.PunchCardSection
import com.example.financeapp.goalhistoryscreen.TotalGoalsAchievedSection
import com.example.financeapp.goalhistoryscreen.TotalGoalsAchievedSectionViewModel
import com.example.financeapp.goalhistoryscreen.TotalTokensEarnedSection
import com.example.financeapp.repositories.ShopRepository
import com.example.financeapp.homescreen.ShopSection
import com.example.financeapp.header.HeaderSection
import com.example.financeapp.header.HeaderSectionViewModel
import com.example.financeapp.homescreen.DailyTipSection
import com.example.financeapp.homescreen.GoalprogressSection
import com.example.financeapp.homescreen.GoalsSection
import com.example.financeapp.homescreen.GoalsSectionViewModel
import com.example.financeapp.homescreen.QuoteSection
import com.example.financeapp.homescreen.RecentlyCompletedGoalsSection
import com.example.financeapp.homescreen.SavedReceiptsSection
import com.example.financeapp.homescreen.TokenBanner
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
import com.example.financeapp.repositories.AdRepository
import com.example.financeapp.shopscreen.ThemeShopIntroSection
import com.example.financeapp.shopscreen.ThemeShopSection
import com.example.financeapp.shopscreen.ThemeShopViewModel
import com.example.financeapp.ui.theme.AppColors
import com.example.financeapp.ui.theme.AzureAppColors
import com.example.financeapp.ui.theme.CharcoalAppColors
import com.example.financeapp.ui.theme.ElectricAppColors
import com.example.financeapp.ui.theme.FinanceAppTheme
import com.example.financeapp.ui.theme.GreenAppColors
import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.ui.theme.PeachAppColors

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
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


enum class Screen (id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3),
    SPLASH(4),
    RECEIPTS(5),

    ABOUT_US(6),

    USER_SETTINGS(id = 7),

    SHOP (id = 8)
}

enum class TutorialStep (id: Int) {

    NONE (-1),
    HOMESCREEN_START (0),
    HOMESCREEN_RECENTLY_COMPLETED_GOALS (1),
    HOMESCREEN_CURRENT_GOALS (2),
    CURRENT_GOALS_BUTTON (3),
    HOMESCREEN_CURRENT_GOAL (4),
    HOMESCREEN_QUOTE (5),
    HOMESCREEN_SAVED_RECEIPTS (6),
    HOMESCREEN_END (7),

    RECEIPTS_START(8),
    RECEIPTS_AVERAGE_SECTION(9),
    RECEIPTS_SUM_SECTION(10),
    RECEIPTS_LOG_SECTION(11),
    RECEIPTS_TAKE_PICTURE(12),
    RECEIPTS_END(13),
    GOALS_START (14),
    GOALS_PUNCHCARD (15),
    GOALS_TOTAL_GOALS (16),
    GOALS_TOTAL_TOKENS (17),
    GOALS_ACHIEVEMENTS (18),
    GOALS_END (19)
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

            val appColorsState = remember { mutableStateOf(GreenAppColors) }

            FinanceAppTheme (
                appColors = appColorsState
            ) {
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

                            val database = FinanceAppDatabase.getInstance(context)
                            val repository = GoalRepository(database)
                            return TotalGoalsAchievedSectionViewModel(repository) as T
                        }
                    }
                )

                val themeShopViewModel: ThemeShopViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun <T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val billingManager = BillingManager(context)
                            val repository = ShopRepository(database, billingManager)
                            return ThemeShopViewModel(billingManager = billingManager, shopRepository = repository) as T
                        }
                    }
                )

                var tutorialInformation by remember { mutableStateOf(value = TutorialInformation(false, TutorialStep.NONE))}

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

                var previewColors by remember { mutableStateOf<AppColors?>(null) } //um ein Preview eines Themes anzuzeigen

                CompositionLocalProvider (
                    LocalAppColors provides (previewColors ?: appColorsState.value)
                ) {
                    Column (
                        modifier = Modifier
                            .background(previewColors?.background ?: appColorsState.value.background) //damit der verkackte Streifen zwischen HeaderSection und den darunterliegenden Sections auch eingefärbt wird
                            .fillMaxSize()
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (sectionIdentifier == Screen.HOME) {

                                    tutorialInformation = tutorialInformation.copy(
                                        isActive = true,
                                        tutorialStep = when (tutorialInformation.tutorialStep) {
                                            TutorialStep.HOMESCREEN_START -> TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS
                                            TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS -> TutorialStep.HOMESCREEN_CURRENT_GOALS
                                            TutorialStep.HOMESCREEN_CURRENT_GOALS -> TutorialStep.HOMESCREEN_CURRENT_GOAL
                                            TutorialStep.HOMESCREEN_CURRENT_GOAL -> TutorialStep.HOMESCREEN_QUOTE
                                            TutorialStep.HOMESCREEN_QUOTE -> TutorialStep.HOMESCREEN_END
                                            else -> TutorialStep.HOMESCREEN_END
                                        }
                                    )

                                    if (tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_END) {
                                        tutorialInformation = tutorialInformation.copy(
                                            isActive = false,
                                            tutorialStep = TutorialStep.RECEIPTS_START
                                        )

                                        mainActivityViewModel.setHomeScreenTutorialDone()
                                    }
                                } else if (sectionIdentifier == Screen.RECEIPTS) {

                                    if (mainActivityViewModel.getReceiptsTutorialDone())
                                        return@clickable

                                    //Das ist hier, um den User zu zwingen, während des Tutorials in der ReceiptsSection -> AverageSpentSection
                                    //auch wirklich ein Bild von einem Receipt aufzunehmen :D -> Das stärkt die Retention

                                    if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_TAKE_PICTURE)
                                        return@clickable

                                    tutorialInformation = tutorialInformation.copy(
                                        isActive = true,
                                        tutorialStep = when (tutorialInformation.tutorialStep) {
                                            TutorialStep.RECEIPTS_START -> TutorialStep.RECEIPTS_TAKE_PICTURE
                                            TutorialStep.RECEIPTS_TAKE_PICTURE -> TutorialStep.RECEIPTS_LOG_SECTION
                                            TutorialStep.RECEIPTS_LOG_SECTION -> TutorialStep.RECEIPTS_SUM_SECTION
                                            TutorialStep.RECEIPTS_SUM_SECTION -> TutorialStep.RECEIPTS_END
                                            else -> TutorialStep.RECEIPTS_START
                                        }
                                    )

                                    if (tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_END) {
                                        tutorialInformation = tutorialInformation.copy(
                                            isActive = false,
                                            tutorialStep = TutorialStep.GOALS_START
                                        )

                                        mainActivityViewModel.setReceiptsTutorialDone()
                                    }

                                } else if (sectionIdentifier == Screen.GOALHISTORY) {

                                    if (mainActivityViewModel.getGoalHistoryTutorialDone())
                                        return@clickable

                                    tutorialInformation = tutorialInformation.copy(
                                        isActive = true,
                                        tutorialStep = when (tutorialInformation.tutorialStep) {
                                            TutorialStep.GOALS_START -> TutorialStep.GOALS_PUNCHCARD
                                            TutorialStep.GOALS_PUNCHCARD -> TutorialStep.GOALS_TOTAL_GOALS
                                            TutorialStep.GOALS_TOTAL_GOALS -> TutorialStep.GOALS_TOTAL_TOKENS
                                            TutorialStep.GOALS_TOTAL_TOKENS -> TutorialStep.GOALS_ACHIEVEMENTS
                                            TutorialStep.GOALS_ACHIEVEMENTS -> TutorialStep.GOALS_END
                                            else -> TutorialStep.GOALS_START
                                        }
                                    )

                                    if (tutorialInformation.tutorialStep == TutorialStep.GOALS_END) {
                                        tutorialInformation = tutorialInformation.copy(
                                            isActive = false,
                                            tutorialStep = TutorialStep.GOALS_START
                                        )

                                        mainActivityViewModel.setGoalHistoryTutorialDone()
                                    }
                                }
                            }
                    ) {
                        // Header nur, wenn nicht Welcome
                        if (listOf<Screen>(Screen.HOME, Screen.LIKEDQUOTES, Screen.GOALHISTORY, Screen.RECEIPTS, Screen.ABOUT_US, Screen.USER_SETTINGS, Screen.SHOP).contains(sectionIdentifier)) {

                            if (goalAchieved) {

                            } else {

                                HeaderSection (
                                    onNewSectionIdentifier = {
                                        sectionIdentifier = it
                                        previewColors = null
                                    },
                                    sectionIdentifier = sectionIdentifier.ordinal,
                                    tutorialInformation = tutorialInformation,
                                    headerSectionViewModel = headerSectionViewModel
                                )

                                Spacer (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
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
                                tutorialInformation = tutorialInformation,
                                receiptSectionsViewModel = receiptSectionsViewModel,
                                goalsSectionViewModel = goalSectionViewModel,
                                onGoalAchieved = {
                                    goalAchieved = true
                                },
                                onWellDoneSectionDismissed = {
                                    goalAchieved = false
                                },
                                shopSectionClicked = {
                                    sectionIdentifier = Screen.SHOP
                                }
                            )
                        }

                        if (sectionIdentifier == Screen.LIKEDQUOTES)
                            LikedQuotesSection (
                                tutorialInformation = tutorialInformation
                            )

                        if (sectionIdentifier == Screen.GOALHISTORY)
                            GoalHistorySection (
                                totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                                mainActivityViewModel = mainActivityViewModel,
                                tutorialInformation = tutorialInformation
                            )

                        if (sectionIdentifier == Screen.RECEIPTS)
                            ReceiptsSection (
                                onReceiptAdded = {
                                    tutorialInformation = tutorialInformation.copy (
                                        isActive = tutorialInformation.isActive,
                                        tutorialStep = if (tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_TAKE_PICTURE) TutorialStep.RECEIPTS_LOG_SECTION else TutorialStep.NONE
                                    )
                                },
                                receiptSectionsViewModel = receiptSectionsViewModel,
                                mainActivityViewModel = mainActivityViewModel,
                                tutorialInformation = tutorialInformation
                            )

                        if (sectionIdentifier == Screen.USER_SETTINGS)
                            SettingsScreen (
                                headerSectionViewModel = headerSectionViewModel,
                                tutorialInformation = tutorialInformation
                            )

                        if (sectionIdentifier == Screen.SHOP) {
                            ShopScreen (
                                headerSectionViewModel = headerSectionViewModel,
                                themeShopViewModel = themeShopViewModel,
                                tutorialInformation = tutorialInformation,
                                previewRequested = { theme ->

                                    previewColors = if (theme == "Charcoal") {
                                        CharcoalAppColors
                                    }
                                    else if (theme == "Electric")
                                        ElectricAppColors
                                    else if (theme == "Azure")
                                        AzureAppColors
                                    else if (theme == "Peach")
                                        PeachAppColors
                                    else
                                        GreenAppColors
                                },
                                applyThemeRequested = { theme ->
                                    previewColors = null

                                    appColorsState.value = if (theme == "Charcoal") {
                                        CharcoalAppColors
                                    }
                                    else if (theme == "Electric")
                                        ElectricAppColors
                                    else if (theme == "Azure")
                                        AzureAppColors
                                    else if (theme == "Peach")
                                        PeachAppColors
                                    else
                                        GreenAppColors
                                }
                            )
                        }

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

                    if (sectionIdentifier == Screen.HOME)
                    {
                        if (!mainActivityViewModel.getHomeScreenTutorialDone() && !tutorialInformation.isActive) {
                            tutorialInformation = tutorialInformation.copy (
                                isActive = true,
                                tutorialStep = TutorialStep.HOMESCREEN_START
                            )
                        }

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_START) {
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
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS) {
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
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_CURRENT_GOALS) {
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
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_CURRENT_GOAL) {
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
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_QUOTE) {
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
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_END) {
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
                    } else if (sectionIdentifier == Screen.RECEIPTS) {

                        if (!mainActivityViewModel.getReceiptsTutorialDone() && !tutorialInformation.isActive) {
                            tutorialInformation = tutorialInformation.copy (
                                isActive = true,
                                tutorialStep = TutorialStep.RECEIPTS_START
                            )
                        }

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_START) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(75.dp)
                                    )

                                    Text (
                                        text = "Welcome to your receipt tracking space!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_TAKE_PICTURE) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(100.dp)
                                    )

                                    Text (
                                        text = "Take a picture of your receipt to always have an eye on your finances.\n\nGive it a try!\n\nCheck the Remind-me button and you'll be notified by us.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_SUM_SECTION) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(75.dp)
                                    )

                                    Text (
                                        text = "This sections helps you to keep an eye on your expenses.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_LOG_SECTION) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(275.dp)
                                    )

                                    Text (
                                        text = "Just scroll down when you need to revisit one of your receipts.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else if (sectionIdentifier == Screen.GOALHISTORY) {

                        if (!mainActivityViewModel.getGoalHistoryTutorialDone() && !tutorialInformation.isActive) {
                            tutorialInformation = tutorialInformation.copy (
                                isActive = true,
                                tutorialStep = TutorialStep.GOALS_START
                            )
                        }

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_START) {
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
                                        text = "Welcome to your goal overview space!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_PUNCHCARD) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(75.dp)
                                    )

                                    Text (
                                        text = "Fill the punch card and treat your self BIG when done.\n\nA holiday, a present to yourself - \nthe world is your oyster!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_TOTAL_GOALS) {
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
                                        text = "The more goals the better ;)",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_TOTAL_TOKENS) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer (
                                        modifier = Modifier
                                            .height(275.dp)
                                    )

                                    Text (
                                        text = "The more token the better, too!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_ACHIEVEMENTS) {
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
                                        text = "Remind yourself from time to time about your achievements and successes!",
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
    }
}

@Composable
fun HomeScreen(tutorialInformation: TutorialInformation, receiptSectionsViewModel: ReceiptSectionsViewModel, goalsSectionViewModel: GoalsSectionViewModel, onGoalAchieved: () -> Unit, onWellDoneSectionDismissed: () -> Unit, shopSectionClicked: () -> Unit, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    var goalAchieved by remember { mutableStateOf(false) }
    var idGoalAchieved by remember { mutableIntStateOf(0)}

    Column (
        modifier = Modifier
            .background (
                color = colors.background
            ),
        verticalArrangement = Arrangement.Top
    ) {

        if (goalAchieved) {

            onGoalAchieved()

            WellDoneSection (
                modifier = Modifier
                    .fillMaxHeight(),
                goalsSectionViewModel = goalsSectionViewModel,
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
                GoalprogressSection (
                    modifier = Modifier
                        .weight(1f),
                    onGoalAchieved = { idGoal ->
                        goalAchieved = true
                        idGoalAchieved = idGoal
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

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .background (
                            color = colors.background,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Row (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DailyTipSection (
                            modifier = Modifier
                                .weight(3f)
                                .fillMaxHeight(),
                            dailyTipSectionClicked = {

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
                            shopSectionClicked = {
                                shopSectionClicked()
                            }
                        )
                    }
                }

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
                        tutorialInformation = tutorialInformation,
                        onReceiptsLogoClicked = {

                        }
                    )
                }

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                TokenBanner (
                    modifier = Modifier
                        .weight(0.5f),
                    goalsSectionViewModel = goalsSectionViewModel,
                    tutorialInformation = tutorialInformation)

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
fun GoalHistorySection(totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel, mainActivityViewModel: MainActivityViewModel, tutorialInformation: TutorialInformation) {

    val colors = LocalAppColors.current

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
            Row (
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TotalGoalsAchievedSection (
                    modifier = Modifier
                        .weight(1f),
                    totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                    tutorialInformation = tutorialInformation
                )

                TotalTokensEarnedSection (
                    modifier = Modifier
                        .weight(1f),
                    totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                    tutorialInformation = tutorialInformation
                )
            }

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background (
                        color = colors.background,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border (
                        color = colors.surface,
                        width = 4.dp,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = "photo memory random\ntreat pic cycle",
                    color = colors.textPrimary,
                    textAlign = TextAlign.Center
                )
            }
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

@Composable
fun ReceiptsSection(onReceiptAdded:() -> Unit, receiptSectionsViewModel: ReceiptSectionsViewModel, mainActivityViewModel: MainActivityViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    var timespan by remember { mutableStateOf(Timespan.THIS_MONTH) }
    val activity = context as? Activity
    var receiptAdded by remember { mutableStateOf(false) }

    LaunchedEffect(receiptAdded) {
        activity?.let {
            if (!receiptAdded)
                return@LaunchedEffect

            if (!mainActivityViewModel.getReceiptsTutorialDone())
                return@LaunchedEffect

            if (receiptSectionsViewModel.interstitialAdAfterReceiptSeen())
                return@LaunchedEffect

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
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
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
                    onReceiptAdded()
                },
                onDismissRequest = {

                },
                receiptSectionsViewModel = receiptSectionsViewModel,
                tutorialInformation = tutorialInformation
            )

            ExpensesOverviewSection (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                timespan,
                receiptSectionsViewModel,
                tutorialInformation = tutorialInformation
            )
        }

        ReceiptLogSection (
            modifier = Modifier
                .fillMaxHeight(0.8f),
            timespan = timespan,
            receiptSectionsViewModel = receiptSectionsViewModel,
            tutorialInformation = tutorialInformation
        )

        AdSectionLargeBanner (
            tutorialInformation = tutorialInformation
        )
    }
}

@Composable
fun ShopScreen(headerSectionViewModel: HeaderSectionViewModel, themeShopViewModel: ThemeShopViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current, previewRequested: (theme: String) -> Unit, applyThemeRequested: (theme: String) -> Unit) {

    val colors = LocalAppColors.current

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background (
                color = colors.background,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ThemeShopIntroSection (
            modifier = Modifier
                .weight(1f)
        )

        Spacer (
            modifier = Modifier
                .padding (
                    2.dp
                )
        )

        ThemeShopSection (
            modifier = Modifier
                .weight(5f),
            themeShopViewModel = themeShopViewModel,
            previewRequested = { theme ->
                previewRequested(theme)
            },
            applyThemeRequested = { theme ->
                applyThemeRequested(theme)
            }
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        AdSectionMiddleBanner(tutorialInformation = tutorialInformation)
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

