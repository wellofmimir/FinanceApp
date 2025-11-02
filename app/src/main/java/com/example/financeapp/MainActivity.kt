package com.example.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.google.android.gms.ads.MobileAds

enum class Screen(id: Int) {
    HOME(0),
    LIKEDQUOTES(1),
    WELCOME(2),
    GOALHISTORY(3)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            

            MobileAds.initialize(this)

            var sectionIdentifier by remember { mutableStateOf(Screen.WELCOME) }

            Column(
                modifier = Modifier
                    .background(Emerald)
                    .fillMaxSize()
            ) {
                // Header nur, wenn nicht Welcome
                if (sectionIdentifier != Screen.WELCOME) {
                    HeaderSection(onNewSectionIdentifier = { sectionIdentifier = it })
                    Spacer(modifier = Modifier.height(1.dp))
                }

                // Aktueller Screen
                when (sectionIdentifier) {
                    Screen.WELCOME -> WelcomeScreen (
                        onFinished = { sectionIdentifier = Screen.HOME }
                    )
                    Screen.HOME -> HomeScreen()
                    Screen.LIKEDQUOTES -> LikedQuotesScreen()
                    else -> HomeScreen()
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