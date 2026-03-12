package studio.lemniscate.greeen.header

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.Screen
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.ui.theme.LocalAppColors

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import studio.lemniscate.greeen.ui.theme.LocalAppTypography
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HeaderSection (
    onNewSectionIdentifier: (Screen) -> Unit,
    sectionIdentifier: Int,
    tutorialInformation: TutorialInformation,
    headerSectionViewModel: HeaderSectionViewModel,
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    //Das hier erzeugt einfach nur das QuoteViewModel und übergibt dem direkt ein Datenbank-Objekt.
    //Das Datenbank-Objekt braucht dringend den Context, um die SQLite-Datei irgendwo anzulegen.
    //Aber der Context darf nicht in dem HeaderSectionViewModel selbst angelegt werden (geht nicht in Compose).

    headerSectionViewModel.getUser()
    val username = headerSectionViewModel.user.collectAsState()
    var headerText by remember { mutableStateOf("Test") }

    val fontMultiplicator = when (username.value.length) {
        in 1..7 -> 1f
        in 8 .. 12 -> 1f
        in 13 .. 18 ->  0.8f
        in 19 .. 23 -> 0.65f
        else ->  0.5f
    }

    headerText = when (sectionIdentifier) {

        0 -> if (fontMultiplicator < 1f) "Hey ${username.value},\nwhat's up?" else "Hey ${username.value}, what's up?"
        1 -> "Your Liked Quotes"
        2 -> "Goals Completed"
        3 -> "Goals Completed"
        5 -> "Saved Receipts"
        6 -> "About"
        7 -> "Settings"
        8 -> "Shop"
        9 -> "Daily Tips"
        else -> "Welcome!"
    }

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (tutorialInformation.isActive) 0.1f else 1.0f)
            .background (
                color = colors.secondary,
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
            color = colors.primary,
            fontSize = typography.small,
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


                val fontSize = if (sectionIdentifier == 0)
                        typography.title * fontMultiplicator
                    else
                        typography.title

                Text (
                    text = headerText,
                    color = colors.primary,
                    fontSize = fontSize,
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
                    colorFilter = ColorFilter.tint(colors.primary),
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

                DreiPunkteMenu (
                    expanded = expanded,
                    onDismissRequested = {
                        expanded = false
                    },
                    onOverviewClicked = {
                        onNewSectionIdentifier(Screen.HOME)
                        expanded = false
                    },
                    onGoalHistoryClicked = {
                        onNewSectionIdentifier(Screen.GOALHISTORY)
                        expanded = false
                    },
                    onYourQuotesClicked = {
                        onNewSectionIdentifier(Screen.LIKEDQUOTES)
                        expanded = false
                    },
                    onReceiptsClicked = {
                        onNewSectionIdentifier(Screen.RECEIPTS)
                        expanded = false
                    },
                    onSettingsClicked = {
                        onNewSectionIdentifier(Screen.USER_SETTINGS)
                        expanded = false
                    },
                    onAboutClicked = {
                        onNewSectionIdentifier(Screen.ABOUT_US)
                        expanded = false
                    }
                )
            }
        }
    }
}