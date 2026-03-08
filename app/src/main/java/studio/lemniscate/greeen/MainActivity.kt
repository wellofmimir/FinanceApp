package studio.lemniscate.greeen

import com.google.android.gms.ads.MobileAds

import studio.lemniscate.greeen.advertisement.InterstitialAdManager
import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.billingmanager.BillingManager
import studio.lemniscate.greeen.goalhistoryscreen.TotalGoalsAchievedSectionViewModel
import studio.lemniscate.greeen.repositories.ShopRepository
import studio.lemniscate.greeen.shopscreen.ShopScreen
import studio.lemniscate.greeen.header.HeaderSection
import studio.lemniscate.greeen.header.HeaderSectionViewModel
import studio.lemniscate.greeen.homescreen.GoalsSectionViewModel
import studio.lemniscate.greeen.likedquotesscreen.LikedQuotesSection
import studio.lemniscate.greeen.receiptsscreen.ReceiptSectionsViewModel
import studio.lemniscate.greeen.repositories.GoalRepository
import studio.lemniscate.greeen.repositories.ReceiptRepository
import studio.lemniscate.greeen.repositories.UserRepository
import studio.lemniscate.greeen.welcomescreen.WelcomeScreen
import studio.lemniscate.greeen.repositories.AdRepository
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel
import studio.lemniscate.greeen.dailytipscreen.DailyTipScreen
import studio.lemniscate.greeen.dailytipscreen.DailyTipScreenViewModel
import studio.lemniscate.greeen.goalhistoryscreen.GoalHistoryScreen
import studio.lemniscate.greeen.homescreen.AchievementsSectionViewModel
import studio.lemniscate.greeen.homescreen.HomeScreen
import studio.lemniscate.greeen.homescreen.QuoteViewModel
import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.homescreen.*
import studio.lemniscate.greeen.receiptsscreen.ReceiptScreen
import studio.lemniscate.greeen.repositories.CurrencyRepository
import studio.lemniscate.greeen.repositories.DailyTipRepository
import studio.lemniscate.greeen.repositories.FeedbackRepository
import studio.lemniscate.greeen.repositories.QuoteRepository
import studio.lemniscate.greeen.settingsscreen.SettingsScreen
import studio.lemniscate.greeen.settingsscreen.SettingsViewModel
import studio.lemniscate.greeen.ui.theme.AppColors
import studio.lemniscate.greeen.ui.theme.AzureAppColors
import studio.lemniscate.greeen.ui.theme.CharcoalAppColors
import studio.lemniscate.greeen.ui.theme.ElectricAppColors
import studio.lemniscate.greeen.ui.theme.GreeenAppTheme
import studio.lemniscate.greeen.ui.theme.GreenAppColors
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.advertisement.AdSectionMiddleBanner
import studio.lemniscate.greeen.advertisement.RewardedAdManager
import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.goalhistoryscreen.PunchCardSectionViewModel
import studio.lemniscate.greeen.aboutscreen.AboutScreen
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.commonutils.GlobalToastHandler
import studio.lemniscate.greeen.repositories.BadgesRepository
import studio.lemniscate.greeen.metricsscreen.MetricsScreenViewModel
import studio.lemniscate.greeen.repositories.MetricsRepository
import studio.lemniscate.greeen.ui.theme.BordeauxAppColors
import studio.lemniscate.greeen.welcomescreen.WelcomeScreenViewModel

import android.app.NotificationChannel
import android.app.NotificationManager

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.core.tween

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import androidx.compose.material3.Text
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.ui.text.style.TextDecoration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.delay
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.notifications.DailyEvents


enum class Screen (id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3),
    SPLASH(4),
    RECEIPTS(5),

    ABOUT_US(6),

    USER_SETTINGS(id = 7),

    SHOP (id = 8),

    DAILY_TIPS(id = 9),
    ADD_NEW_RECEIPT (id = 11),
    NONE (id = 10)
}

fun Screen.toPage(): Int = when (this) {
    Screen.GOALHISTORY -> 1
    Screen.HOME -> 2
    Screen.RECEIPTS -> 3
    Screen.DAILY_TIPS -> 4
    Screen.SHOP -> 5
    Screen.LIKEDQUOTES -> 6
    Screen.USER_SETTINGS -> 7
    Screen.ABOUT_US -> 8
    else -> 2
}

fun Int.toScreen(): Screen = when (this) {
    1 -> Screen.GOALHISTORY
    2 -> Screen.HOME
    3 -> Screen.RECEIPTS
    4 -> Screen.DAILY_TIPS
    5 -> Screen.SHOP
    6 -> Screen.LIKEDQUOTES
    7 -> Screen.USER_SETTINGS
    8 -> Screen.ABOUT_US
    else -> Screen.HOME
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
    HOMESCREEN_DAILY_FINANCIAL_TIP (20),
    HOMESCREEN_SHOP (21),
    HOMESCREEN_TOKEN_BANNER (22),
    HOMESCREEN_END (7),

    RECEIPTS_START(8),
    RECEIPTS_AVERAGE_SECTION(9),

    RECEIPTS_SEE_METRICS_SECTION(24),
    RECEIPTS_SUM_SECTION(10),
    RECEIPTS_LOG_SECTION(11),
    RECEIPTS_TAKE_PICTURE(12),

    RECEIPTS_RANDOM_MEMORY(23),
    RECEIPTS_END(13),
    GOALS_START (14),
    GOALS_PUNCHCARD (15),
    GOALS_TOTAL_GOALS (16),
    GOALS_TOTAL_TOKENS (17),
    GOALS_ACHIEVEMENTS (18),
    GOALS_END (19)
}



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            val appColorsState = remember { mutableStateOf(GreenAppColors) }

            GreeenAppTheme (
                appColors = appColorsState
            ) {
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    MobileAds.initialize(context)
                    InterstitialAdManager.instance.initialize(context)
                    InterstitialAdManager.instance.loadInterstitial(context)
                }

                val manager = getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(NotificationChannel("quotes", "Quote", NotificationManager.IMPORTANCE_HIGH))
                manager.createNotificationChannel(NotificationChannel("receipts", "Receipt", NotificationManager.IMPORTANCE_HIGH))
                manager.createNotificationChannel(NotificationChannel("reminders", "Reminders", NotificationManager.IMPORTANCE_HIGH))
                manager.createNotificationChannel(NotificationChannel("tips", "Tips", NotificationManager.IMPORTANCE_HIGH))

                val mainActivityViewModel: MainActivityViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val repository = UserRepository.getInstance(database)

                            return MainActivityViewModel(repository) as T
                        }
                    }
                )

                val welcomeScreenViewModel: WelcomeScreenViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.Companion.getInstance(context)
                            val userRepository = UserRepository.getInstance(database)
                            val goalRepository = GoalRepository.getInstance(database)

                            return WelcomeScreenViewModel(userRepository, goalRepository) as T
                        }
                    }
                )

                val achievementsSectionViewModel: AchievementsSectionViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val repository = GoalRepository.getInstance(database)

                            return AchievementsSectionViewModel(repository) as T
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
                            val repository = GoalRepository.getInstance(database)

                            return GoalsSectionViewModel(repository) as T
                        }
                    }
                )

                val totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun <T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val repository = GoalRepository.getInstance(database)

                            return TotalGoalsAchievedSectionViewModel(repository) as T
                        }
                    }
                )

                val themeShopViewModel: ThemeShopViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun <T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val billingManager = BillingManager(context)
                            val repository = ShopRepository.getInstance(database, billingManager)

                            return ThemeShopViewModel(billingManager = billingManager, shopRepository = repository) as T
                        }
                    }
                )

                val dailyTipScreenViewModel: DailyTipScreenViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {

                            val fileProvider = FileProvider(context)
                            val rewardedAdManager = RewardedAdManager(context)
                            rewardedAdManager.load("ca-app-pub-3940256099942544/5224354917")

                            val database = FinanceAppDatabase.getInstance(context)
                            val repository = DailyTipRepository.getInstance(database, fileProvider)

                            return DailyTipScreenViewModel(rewardedAdManager, repository) as T
                        }
                    }
                )

                val quoteViewModel: QuoteViewModel = viewModel (
                    factory = remember {
                        object: ViewModelProvider.Factory {
                            override fun <T: ViewModel> create(modelClass: Class<T>): T {

                                val database = FinanceAppDatabase.getInstance(context)
                                val repository = QuoteRepository.getInstance(database)

                                return QuoteViewModel(repository) as T
                            }
                        }
                    }
                )

                val settingsViewModel: SettingsViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun <T: ViewModel> create(modelClass: Class<T>): T {
                            val database = FinanceAppDatabase.getInstance(context)

                            val feedbackRepository = FeedbackRepository.getInstance(database)
                            val currencyRepository = CurrencyRepository.getInstance(database)

                            return SettingsViewModel(feedbackRepository, currencyRepository) as T
                        }
                    }
                )

                val punchCardSectionViewModel: PunchCardSectionViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val database = FinanceAppDatabase.getInstance(context)
                            val goalRepository = GoalRepository.getInstance(database)

                            return PunchCardSectionViewModel(goalRepository) as T
                        }
                    }
                )

                val badgesViewModel: BadgesViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val fileProvider = FileProvider(context)
                            val database = FinanceAppDatabase.getInstance(context)
                            val badgesRepository = BadgesRepository.getInstance(database, fileProvider)

                            return BadgesViewModel(badgesRepository) as T
                        }
                    }
                )

                val metricsScreenViewModel: MetricsScreenViewModel = viewModel (
                    factory = object: ViewModelProvider.Factory {
                        override fun<T: ViewModel> create(modelClass: Class<T>): T {

                            val rewardedAdManager = RewardedAdManager(context)
                            rewardedAdManager.load("ca-app-pub-3940256099942544/5224354917")

                            val database = FinanceAppDatabase.getInstance(context)
                            val metricsRepository = MetricsRepository.getInstance(database)

                            return MetricsScreenViewModel(rewardedAdManager, metricsRepository) as T
                        }
                    }
                )

                GlobalToastHandler (
                    badgesViewModel = badgesViewModel,
                    goalsSectionViewModel = goalSectionViewModel,
                    receiptSectionsViewModel = receiptSectionsViewModel,
                    quoteViewModel = quoteViewModel
                )

                LaunchedEffect(Unit) {
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FIRST_RECEIPT)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.THIRTY_RECEIPTS)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.HUNDRED_RECEIPTS)

                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FIRST_GOAL)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.TEN_GOALS)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FIFTY_GOALS)

                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FIRST_QUOTE_LIKED)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FOURTEEN_QUOTES_LIKED)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FORTY_QUOTES_LIKED)

                    badgesViewModel.fetchWallpaper(BadgeIdentifier.FIRST_DAILY_TIP_LIKED)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED)
                    badgesViewModel.fetchWallpaper(BadgeIdentifier.NINETY_DAILY_TIPS_LIKED)

                    settingsViewModel.getCurrency()
                }

                var tutorialInformation by remember { mutableStateOf(value = TutorialInformation(false, TutorialStep.NONE))}

                mainActivityViewModel.loadUser()
                val user by mainActivityViewModel.user.collectAsState()

                var sectionIdentifier by remember { mutableStateOf(Screen.SPLASH)}
                var goalAchieved by remember { mutableStateOf(false) }
                val addReceiptMenuOpen by receiptSectionsViewModel.showAddReceiptSection.collectAsState()

                val adremoverActive by themeShopViewModel.adRemoverPurchased.collectAsState()
                val appliedTheme by themeShopViewModel.appliedTheme.collectAsState()
                var previewColors by remember { mutableStateOf<AppColors?>(null) } //um ein Preview eines Themes anzuzeigen
                var isPagerBlocked by remember { mutableStateOf(false) }

                val pagerState = rememberPagerState (
                    initialPage = 2,
                    pageCount = { 9 }
                )

                LaunchedEffect(Unit) {
                    sectionIdentifier = when (user) {
                        "DUMMY", "" -> Screen.WELCOME
                        else -> Screen.SPLASH
                    }

                    delay(2000)

                    if (sectionIdentifier == Screen.SPLASH)
                        sectionIdentifier = Screen.HOME
                }

                LaunchedEffect(pagerState.currentPage) {
                    if (sectionIdentifier == Screen.SPLASH || sectionIdentifier == Screen.WELCOME)
                        return@LaunchedEffect

                    sectionIdentifier = pagerState.currentPage.toScreen()
                    previewColors = null
                }

                LaunchedEffect(sectionIdentifier) {
                    if (sectionIdentifier == Screen.SPLASH || sectionIdentifier == Screen.WELCOME)
                        return@LaunchedEffect

                    pagerState.scrollToPage(sectionIdentifier.toPage())
                }

                LaunchedEffect(Unit) {
                    val currentTheme = mainActivityViewModel.getCurrentTheme()

                    appColorsState.value = if (currentTheme == "charcoaltheme") {
                        CharcoalAppColors
                    }
                    else if (currentTheme == "electrictheme")
                        ElectricAppColors
                    else if (currentTheme == "azuretheme")
                        AzureAppColors
                    else if (currentTheme == "eleganttheme")
                        BordeauxAppColors
                    else if (currentTheme == "Greeen")
                        GreenAppColors
                    else
                        GreenAppColors

                    themeShopViewModel.setAppliedTheme(currentTheme)
                }

                LaunchedEffect(appliedTheme) {
                    appColorsState.value = if (appliedTheme == "charcoaltheme") {
                        CharcoalAppColors
                    }
                    else if (appliedTheme == "electrictheme")
                        ElectricAppColors
                    else if (appliedTheme == "azuretheme")
                        AzureAppColors
                    else if (appliedTheme == "eleganttheme")
                        BordeauxAppColors
                    else if (appliedTheme == "Greeen")
                        GreenAppColors
                    else
                        GreenAppColors
                }

                CompositionLocalProvider (
                    LocalAppColors provides (previewColors ?: appColorsState.value)
                ) {
                    Column (
                        modifier = Modifier
                            .background(previewColors?.primary ?: appColorsState.value.primary) //damit der verkackte Streifen zwischen HeaderSection und den darunterliegenden Sections auch eingefärbt wird
                            .fillMaxSize()
                            .clickable (
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) {
                                if (sectionIdentifier == Screen.GOALHISTORY) {

                                    if (mainActivityViewModel.getGoalHistoryTutorialDone())
                                        return@clickable

                                    tutorialInformation = tutorialInformation.advanceGoalHistoryScreenTutorial()

                                    if (tutorialInformation.tutorialStep == TutorialStep.GOALS_END) {
                                        tutorialInformation = tutorialInformation.endGoalHistoryScreenTutorial()
                                        mainActivityViewModel.setGoalHistoryTutorialDone()
                                    }
                                }
                            }
                    ) {
                        if (listOf(Screen.HOME, Screen.LIKEDQUOTES, Screen.GOALHISTORY, Screen.RECEIPTS, Screen.ABOUT_US, Screen.USER_SETTINGS, Screen.SHOP, Screen.DAILY_TIPS).contains(sectionIdentifier)) {
                            if (!goalAchieved && !addReceiptMenuOpen) {
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

                        if (sectionIdentifier == Screen.SPLASH || sectionIdentifier == Screen.WELCOME) {
                            AnimatedVisibility (
                                visible = true,
                                enter = fadeIn (
                                    animationSpec = tween (
                                        durationMillis = 2000
                                    )
                                )
                            ) {
                                WelcomeScreen (
                                    onFinished = {
                                        mainActivityViewModel.loadUser()
                                        sectionIdentifier = Screen.HOME
                                    },
                                    welcomeScreenViewModel = welcomeScreenViewModel,
                                    splashMode = sectionIdentifier != Screen.WELCOME
                                )
                            }


                        } else {
                            HorizontalPager (
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                userScrollEnabled = !isPagerBlocked
                            ) { page ->
                                val isSelected = pagerState.currentPage == page && !pagerState.isScrollInProgress

                                when (page) {
                                    1 -> {
                                        if (sectionIdentifier == Screen.GOALHISTORY) {
                                            AnimatedVisibility (
                                                visible = isSelected,
                                                exit = ExitTransition.None
                                            ) {
                                                GoalHistoryScreen (
                                                    goalsSectionViewModel = goalSectionViewModel,
                                                    totalGoalsAchievedSectionViewModel = totalGoalsAchievedSectionViewModel,
                                                    achievementsSectionViewModel = achievementsSectionViewModel,
                                                    shopViewModel = themeShopViewModel,
                                                    punchCardSectionViewModel = punchCardSectionViewModel,
                                                    tutorialInformation = tutorialInformation,
                                                    onPunchCardFilled = {
                                                        goalAchieved = true
                                                        isPagerBlocked = true
                                                    },
                                                    onWellDoneSectionDismissed = {
                                                        goalAchieved = false
                                                        isPagerBlocked = false
                                                    }
                                                )
                                            }
                                        }
                                    }

                                    2 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            HomeScreen (
                                                tutorialInformation = tutorialInformation,
                                                receiptSectionsViewModel = receiptSectionsViewModel,
                                                dailyTipScreenViewModel = dailyTipScreenViewModel,
                                                goalsSectionViewModel = goalSectionViewModel,
                                                quoteViewModel = quoteViewModel,
                                                badgesViewModel = badgesViewModel,
                                                settingsViewModel = settingsViewModel,
                                                onGoalAchieved = {
                                                    goalAchieved = true
                                                    isPagerBlocked = true
                                                },
                                                onWellDoneSectionDismissed = {
                                                    goalAchieved = false
                                                    isPagerBlocked = false
                                                },
                                                shopSectionClicked = {
                                                    sectionIdentifier = Screen.SHOP
                                                },
                                                receiptsSectionClicked = {
                                                    sectionIdentifier = Screen.RECEIPTS
                                                },
                                                recentlyCompletedGoalsSectionClicked = {
                                                    sectionIdentifier = Screen.GOALHISTORY
                                                },
                                                dailyTipsSectionClicked = {
                                                    sectionIdentifier = Screen.DAILY_TIPS
                                                }
                                            )
                                        }
                                    }

                                    3 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            ReceiptScreen (
                                                onReceiptAdded = {
                                                    if (tutorialInformation.isActive) {
                                                        tutorialInformation = tutorialInformation.advanceReceiptScreenTutorial()
                                                        mainActivityViewModel.setHomeScreenTutorialDone()
                                                        receiptSectionsViewModel.closeAddReceiptSection()
                                                    }
                                                },
                                                onAddReceiptMenuOpened = {
                                                    isPagerBlocked = true
                                                },
                                                onAddReceiptMenuClosed = {
                                                    isPagerBlocked = false
                                                },
                                                receiptSectionsViewModel = receiptSectionsViewModel,
                                                mainActivityViewModel = mainActivityViewModel,
                                                shopViewModel = themeShopViewModel,
                                                metricsScreenViewModel = metricsScreenViewModel,
                                                badgesViewModel = badgesViewModel,
                                                tutorialInformation = tutorialInformation
                                            )
                                        }
                                    }

                                    4 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            DailyTipScreen (
                                                dailyTipScreenViewModel = dailyTipScreenViewModel,
                                                shopViewModel = themeShopViewModel,
                                                badgesViewModel = badgesViewModel
                                            )
                                        }
                                    }

                                    5 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            ShopScreen (
                                                themeShopViewModel = themeShopViewModel,
                                                previewRequested = { theme ->

                                                    previewColors = if (theme == "charcoaltheme") {
                                                        CharcoalAppColors
                                                    }
                                                    else if (theme == "electrictheme")
                                                        ElectricAppColors
                                                    else if (theme == "azuretheme")
                                                        AzureAppColors
                                                    else if (theme == "eleganttheme")
                                                        BordeauxAppColors
                                                    else if (theme == "Greeen")
                                                        GreenAppColors
                                                    else
                                                        GreenAppColors
                                                },
                                                applyThemeRequested = { theme ->
                                                    previewColors = null

                                                    appColorsState.value = if (theme == "charcoaltheme") {
                                                        CharcoalAppColors
                                                    }
                                                    else if (theme == "electrictheme")
                                                        ElectricAppColors
                                                    else if (theme == "azuretheme")
                                                        AzureAppColors
                                                    else if (theme == "eleganttheme")
                                                        BordeauxAppColors
                                                    else if (theme == "Greeen")
                                                        GreenAppColors
                                                    else
                                                        GreenAppColors

                                                    mainActivityViewModel.setCurrentTheme(theme)
                                                }
                                            )
                                        }

                                        AdSectionMiddleBanner (
                                            suppressAd = adremoverActive,
                                            tutorialInformation = tutorialInformation
                                        )
                                    }

                                    6 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            LikedQuotesSection (
                                                quoteViewModel = quoteViewModel,
                                                shopViewModel = themeShopViewModel,
                                                tutorialInformation = tutorialInformation
                                            )
                                        }
                                    }

                                    7 -> {
                                        AnimatedVisibility (
                                            visible = isSelected,
                                            exit = ExitTransition.None
                                        ) {
                                            SettingsScreen (
                                                headerSectionViewModel = headerSectionViewModel,
                                                settingsViewModel = settingsViewModel,
                                                shopViewModel = themeShopViewModel,
                                                tutorialInformation = tutorialInformation
                                            )
                                        }
                                    }

                                    8 -> {
                                        AnimatedVisibility (
                                            visible = isSelected
                                        ) {
                                            AboutScreen ()
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (sectionIdentifier == Screen.HOME) {
                        if (!mainActivityViewModel.getHomeScreenTutorialDone() && !tutorialInformation.isActive) {
                            tutorialInformation = tutorialInformation.restartHomeScreenTutorial()
                        }

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_START) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "Welcome to Greeen.\n\n" +
                                                "Here’s a quick guide to help you explore the main features.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "A quick overview of what you’ve recently accomplished.\n\n" +
                                                "Progress adds up faster than you think.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_CURRENT_GOALS) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-200).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "Here you can see the goals you’re actively working on.\n\n" +
                                                "Focus on what matters most right now.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_CURRENT_GOAL) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "This is the goal you’re working on at the moment.\n\nTap the percentage to update your progress.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_QUOTE) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "A small quote to encourage you as you work on your goals.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_DAILY_FINANCIAL_TIP) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "A daily financial insight to help you stay on track with your goals.\n\nMake sure to check it out!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_SHOP) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceHomeScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "Green is not for everyone - we get it.\n\nOur shop offers different themes to color up your experience.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.HOMESCREEN_END) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.endHomeScreenTutorial()
                                        mainActivityViewModel.setHomeScreenTutorialDone()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "You now know the basics — the rest is up to you.\n\nLet’s get started.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    } else if (sectionIdentifier == Screen.RECEIPTS) {
                        if (!mainActivityViewModel.getReceiptsTutorialDone() && !tutorialInformation.isActive)
                            tutorialInformation = tutorialInformation.restartReceiptScreenTutorial()

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_START) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceReceiptScreenTutorial()
                                    },
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
                                        text = "Welcome!\nThis section helps you log purchases and stay on top of your finances.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_SEE_METRICS_SECTION) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceReceiptScreenTutorial()
                                    },
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

                                    Text(
                                        text = "Here you can explore your receipt metrics and get insights into your finances.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }

                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_TAKE_PICTURE) {

                            val tutorialText = if (addReceiptMenuOpen)
                                ""
                            else
                                "Take a picture of your receipt to always have an eye on your finances.\n\nGive it a try!\n\nCheck the Remind-me button and you'll be notified by us."

                            if (!addReceiptMenuOpen) {
                                Box (
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(y = (-30).dp)
                                        .clickable(
                                        ) {
                                            if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_TAKE_PICTURE) {
                                                receiptSectionsViewModel.showAddReceiptSection()
                                                return@clickable
                                            }
                                        },
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
                                            text = tutorialText,
                                            fontSize = 24.sp,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .padding(horizontal = 8.dp)
                                        )

                                        Spacer (
                                            modifier = Modifier
                                                .height(100.dp)
                                        )

                                        Text (
                                            text = "Skip",
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            textDecoration = TextDecoration.Underline,
                                            modifier = Modifier
                                                .clickable() {
                                                    tutorialInformation = tutorialInformation.advanceReceiptScreenTutorial()
                                                }
                                        )
                                    }
                                }
                            }

                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_SUM_SECTION) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.endReceiptsScreenTutorial()
                                        mainActivityViewModel.setReceiptsTutorialDone()
                                    },
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
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.RECEIPTS_LOG_SECTION) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (-30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceReceiptScreenTutorial()
                                        mainActivityViewModel.setHomeScreenTutorialDone()
                                    },
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
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    } else if (sectionIdentifier == Screen.GOALHISTORY) {

                        if (!mainActivityViewModel.getGoalHistoryTutorialDone() && !tutorialInformation.isActive) {
                            tutorialInformation = tutorialInformation.restartGoalHistoryScreenTutorial()
                        }

                        if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_START) {
                            Box (
                                modifier = Modifier
                                    .offset(y = (-30).dp)
                                    .fillMaxSize()
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceGoalHistoryScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column (
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text (
                                        text = "Begin creating and managing your goals here.",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_PUNCHCARD) {
                            Box (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (+30).dp)
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.advanceGoalHistoryScreenTutorial()
                                    },
                                contentAlignment = Alignment.Center
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
                                        text = "Fill the punch card and treat your self BIG when done.\n\nA holiday, a present to yourself - \nthe world is your oyster!",
                                        fontSize = 24.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        } else if (tutorialInformation.isActive && tutorialInformation.tutorialStep == TutorialStep.GOALS_ACHIEVEMENTS) {
                            Box (
                                modifier = Modifier
                                    .offset(y = (-30).dp)
                                    .fillMaxSize()
                                    .clickable(
                                    ) {
                                        tutorialInformation = tutorialInformation.endGoalHistoryScreenTutorial()
                                        mainActivityViewModel.setGoalHistoryTutorialDone()
                                    },
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
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
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


