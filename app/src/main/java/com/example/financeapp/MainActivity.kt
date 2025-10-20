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
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            var sectionIdentifier by remember { mutableStateOf(0) }

            LaunchedEffect(sectionIdentifier) {

                when (sectionIdentifier) {

                    0 -> navController.navigate("HomeScreen") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }

                    1 -> navController.navigate("LikedQuotesScreen") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }

            Column (
                modifier = Modifier
                    .statusBarsPadding()
                    .background (
                        color = Emerald
                    ),
                verticalArrangement = Arrangement.Center
            ){
                HeaderSection(onNewSectionIdentifier = { newSectionIdentifier ->
                    sectionIdentifier = newSectionIdentifier
                })

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                NavHost (
                    navController = navController,
                    startDestination = "HomeScreen"
                ) {
                    composable("HomeScreen") {
                        HomeScreen()
                    }

                    composable("LikedQuotesScreen") {
                        LikedQuotesScreen()
                    }
                }

                Spacer (
                    modifier = Modifier
                        .padding(1.dp)
                )

                AdSection()
            }
        }
    }
}

@Composable
fun HomeScreen() {

    Column (
        modifier = Modifier
            .statusBarsPadding()
            .background(
                color = Emerald
            ),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GoalprogressSection(
                modifier = Modifier.weight(1f)
            )

            QuoteSection(
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(
            modifier = Modifier
                .padding(1.dp)
        )

        GoalsSection()

        Spacer(
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

@Composable
fun AdSection() {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text("Werbung")
        }
    }
}