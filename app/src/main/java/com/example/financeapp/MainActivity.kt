package com.example.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.theme.Emerald

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Column (
                modifier = Modifier
                    .statusBarsPadding()
                    .background (
                        color = Emerald
                    ),
                verticalArrangement = Arrangement.Center
            ){
                HeaderSection()

                Spacer (
                    modifier = Modifier
                        .padding(2.dp)
                )

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ){
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
    }
}
