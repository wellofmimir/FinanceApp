package com.example.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*


enum class Screen (id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3),
    SPLASH(4)
}

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

            mainActivityViewModel.loadUser()
            val user by mainActivityViewModel.user.collectAsState()

            var sectionIdentifier by remember { mutableStateOf(Screen.SPLASH)}

            LaunchedEffect(user) {

                if (sectionIdentifier == Screen.SPLASH)
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
            ) {
                // Header nur, wenn nicht Welcome
                if (listOf<Screen>(Screen.HOME, Screen.LIKEDQUOTES, Screen.GOALHISTORY).contains(sectionIdentifier)) {
                    HeaderSection(onNewSectionIdentifier = {
                        sectionIdentifier = it
                    })
                    Spacer(modifier = Modifier.height(1.dp))
                }

                // Aktueller Screen

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.WELCOME,
                    enter = fadeIn(),
                    exit = fadeOut()
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
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    HomeScreen ()
                }

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.LIKEDQUOTES,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LikedQuotesScreen()
                }

                AnimatedVisibility (
                    visible = sectionIdentifier == Screen.SPLASH,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                    WelcomeScreen (
                        onFinished = {Unit},
                        true
                    )
                }

                Spacer(modifier = Modifier.height(1.dp))

                // AdSection nur auf Home
                if (sectionIdentifier == Screen.HOME) {
                    AdSection()
                }
            }
        }
    }
}

@Composable
fun HomeScreen() {

    Column (
        modifier = Modifier
            .background(
                color = Emerald
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            GoalprogressSection (
                modifier = Modifier.weight(1f)
            )

            QuoteSection (
                modifier = Modifier.weight(1f)
            )
        }

        Spacer (
            modifier = Modifier
                .padding(1.dp)
        )

        GoalsSection()

        Spacer (
            modifier = Modifier
                .padding(1.dp)
        )

        RecemtlyCompletedGoalsSection()
    }
}

@Composable
fun LikedQuotesScreen() {
    LikedQuotesSection()
}