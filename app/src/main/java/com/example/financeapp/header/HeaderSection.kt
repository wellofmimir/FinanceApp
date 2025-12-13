package com.example.financeapp.header

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import com.example.financeapp.header.HeaderSectionViewModel
import com.example.financeapp.R
import com.example.financeapp.Screen
import com.example.financeapp.TutorialInformation
import com.example.financeapp.ui.theme.Emerald
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HeaderSection(onNewSectionIdentifier: (Screen) -> Unit, tutorialInformation: TutorialInformation, headerSectionViewModel: HeaderSectionViewModel, context: Context = LocalContext.current) {

    //Das hier erzeugt einfach nur das QuoteViewModel und übergibt dem direkt ein Datenbank-Objekt.
    //Das Datenbank-Objekt braucht dringend den Context, um die SQLite-Datei irgendwo anzulegen.
    //Aber der Context darf nicht in dem HeaderSectionViewModel selbst angelegt werden (geht nicht in Compose).

    headerSectionViewModel.getUser()
    val username = headerSectionViewModel.user.collectAsState()

    var headerText by remember { mutableStateOf("Test") }
    var sectionIdentifier by remember { mutableStateOf(0) }

    headerText = when (sectionIdentifier) {

        0 -> "Hey ${username.value}, what's up?"
        1 -> "Your Liked Quotes"
        2 -> "Goals Completed"
        3 -> "Saved Receipts"
        4 -> "About Greeen"
        7 -> "Settings"
        else -> "Welcome!"
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
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

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        Text (
            text = formattedDate,
            color = Emerald,
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
                Text (
                    text = headerText,
                    color = Emerald,
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
                    colorFilter = ColorFilter.tint(Emerald),
                    modifier = Modifier
                        .height(40.dp)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (tutorialInformation.isActive)
                                return@clickable

                            expanded = true
                        }
                )

                DreiPunkteMenu(
                    expanded = expanded,
                    onDismissRequested = {
                        expanded = false
                    },
                    onOverviewClicked = {
                        sectionIdentifier = 0
                        onNewSectionIdentifier(Screen.HOME)
                        expanded = false
                    },
                    onGoalHistoryClicked = {
                        sectionIdentifier = 2
                        onNewSectionIdentifier(Screen.GOALHISTORY)
                        expanded = false
                    },
                    onYourQuotesClicked = {
                        sectionIdentifier = 1
                        onNewSectionIdentifier(Screen.LIKEDQUOTES)
                        expanded = false
                    },
                    onReceiptsClicked = {
                        sectionIdentifier = 3
                        onNewSectionIdentifier(Screen.RECEIPTS)
                        expanded = false
                    },
                    onAboutUsClicked = {
                        sectionIdentifier = 4
                        onNewSectionIdentifier(Screen.ABOUT_US)
                        expanded = false
                    },
                    onSettingsClicked = {
                        sectionIdentifier = 7
                        onNewSectionIdentifier(Screen.USER_SETTINGS)
                        expanded = false
                    }
                )
            }
        }
    }
}