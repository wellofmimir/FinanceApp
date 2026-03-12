package studio.lemniscate.greeen.header
import studio.lemniscate.greeen.ui.theme.LocalAppColors

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
import studio.lemniscate.greeen.ui.theme.LocalAppTypography
import studio.lemniscate.greeen.ui.theme.Pistachio


@Composable
fun DreiPunkteMenu (
    expanded: Boolean,
    onDismissRequested: () -> Unit,
    onOverviewClicked: () -> Unit,
    onGoalHistoryClicked: () -> Unit,
    onYourQuotesClicked: () -> Unit,
    onReceiptsClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onAboutClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

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
                            fontSize = typography.button
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
                            fontSize = typography.button
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
                            fontSize = typography.button
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
                            fontSize = typography.button
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
                            fontSize = typography.button
                        )
                    }
                },
                onClick = {
                    onSettingsClicked()
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
                            text = "About",
                            color = Color.White,
                            fontSize = typography.button
                        )
                    }
                },
                onClick = {
                    onAboutClicked()
                }
            )
        }
    }
}