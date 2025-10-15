package com.example.financeapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import kotlin.math.exp


@Composable
fun HeaderSection(context: Context = LocalContext.current) {

    //Das hier erzeugt einfach nur das QuoteViewModel und übergibt dem direkt ein Datenbank-Objekt.
    //Das Datenbank-Objekt braucht dringend den Context, um die SQLite-Datei irgendwo anzulegen.
    //Aber der Context darf nicht in dem HeaderSectionViewModel selbst angelegt werden (geht nicht in Compose).

    val database = remember { FinanceAppDatabase.getInstance(context) }

    val headersectionViewModel: HeaderSectionViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HeaderSectionViewModel(database) as T
            }
        }
    )

    headersectionViewModel.getUser()
    val username = headersectionViewModel.user.collectAsState()

    var headerText by remember { mutableStateOf("Test") }
    var sectionIdentifier by remember { mutableStateOf(0) }

    headerText = when (sectionIdentifier) {

        0 -> "Hey ${username.value}, what's up?"
        1 -> "Your Liked Quotes"
        2 -> "Goals Completed"
        else -> "Welcome!"
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Spacer (
            modifier = Modifier
                .height(32.dp)
        )

        val currentDate = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        Text (
            text = formattedDate,
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box (
                modifier = Modifier
                    .weight(0.9f)
            ) {
                Text(
                    text = headerText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold

                )
            }

            var expanded by remember { mutableStateOf(false) }

            Box (
                modifier = Modifier
                    .weight(0.1f)
            ) {
                Image (
                    painter = painterResource(R.drawable.punktemenu_foreground),
                    contentDescription = "Punktemenu",
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black),
                    modifier = Modifier
                        .height(32.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            expanded = true
                        }
                )

                DreiPunkteMenu (
                    expanded = expanded,
                    onDismissRequested = {
                        expanded = false
                    },
                    onOverviewClicked = {
                        sectionIdentifier = 0
                        expanded = false
                    },
                    onGoalHistoryClicked = {
                        sectionIdentifier = 2
                        expanded = false
                    },
                    onYourQuotesClicked = {
                        sectionIdentifier = 1
                        expanded = false
                    }
                )
            }
        }
    }
}