package com.example.financeapp.header
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import com.example.financeapp.ui.theme.Pistachio


@Composable
fun DreiPunkteMenu(expanded: Boolean,
                   onDismissRequested: () -> Unit,
                   onOverviewClicked: () -> Unit,
                   onGoalHistoryClicked: () -> Unit,
                   onYourQuotesClicked: () -> Unit,
                   onReceiptsClicked: () -> Unit,
                   onSettingsClicked: () -> Unit) {

    val colors = LocalAppColors.current

    DropdownMenu (
        expanded = expanded,
        onDismissRequest = onDismissRequested,
        modifier = Modifier
            .border (
                width = 1.dp,
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        containerColor = Color.Transparent

    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropdownMenuItem (
                text = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Overview",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                onClick = {
                    onOverviewClicked()
                }
            )

            HorizontalDivider (
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )

            DropdownMenuItem (
                text = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Receipts",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                onClick = {
                    onReceiptsClicked()
                }
            )

            HorizontalDivider (
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )

            DropdownMenuItem (
                text = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Goal History",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                onClick = {
                    onGoalHistoryClicked()
                }
            )

            HorizontalDivider (
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )

            DropdownMenuItem (
                text = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Your Quotes",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                onClick = {
                    onYourQuotesClicked()
                }
            )

            HorizontalDivider (
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
            )

            DropdownMenuItem (
                text = {
                    Box (
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text (
                            text = "Settings",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                },
                onClick = {
                    onSettingsClicked()
                }
            )
        }
    }
}