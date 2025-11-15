package com.example.financeapp

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.draw.alpha
import com.example.financeapp.ui.theme.Emerald

@Composable
fun QuoteSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    //Das hier erzeugt einfach nur das QuoteViewModel und übergibt dem direkt ein Datenbank-Objekt.
    //Das Datenbank-Objekt braucht dringend den Context, um die SQLite-Datei irgendwo anzulegen.
    //Aber der Context darf nicht in dem QuoteViewModel selbst angelegt werden (geht nicht in Compose).

    val quoteViewModel: QuoteViewModel = viewModel (
        factory = remember {
            object : ViewModelProvider.Factory {
                override fun <T: ViewModel> create(modelClass: Class<T>): T {
                    val database = FinanceAppDatabase.getInstance(context)
                    val repository = QuoteRepository.getInstance(database)
                    return QuoteViewModel(repository) as T
                }
            }
        }
    )

    val currentDate = java.time.LocalDate.now()
    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
    val formattedDate = currentDate.format(formatter)

    val quoteState = quoteViewModel.quote.collectAsState()
    val quotedPersonState = quoteViewModel.quotedPerson.collectAsState()

    Column (
        modifier = modifier
            .aspectRatio(1f)
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.QUOTE) 0.1f else 1.0f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = Pistachio
                )
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Box (
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = quoteState.value,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.ExtraBold,
                    color = Emerald
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)
                .background(
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = Pistachio
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 8.dp)
            ) {
                var herzZumLikenClicked by remember { mutableStateOf(false) }

                quoteViewModel.getLikedQuotes().forEach { quote ->
                    if (quote.quote == quoteState.value)
                        herzZumLikenClicked = true
                }

                Image (
                    painter = painterResource(com.example.financeapp.R.drawable.herzzumliken_foreground),
                    contentDescription = "HerzZumLiken",
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(if (herzZumLikenClicked) Color.Red else Color.Black),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){
                            herzZumLikenClicked = !herzZumLikenClicked
                            quoteViewModel.quoteGotLiked(quoteState.value, quotedPersonState.value)
                        }
                )
            }

            Box (
                modifier = Modifier
                    .padding(4.dp, end = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text (
                    text = quotedPersonState.value,
                    fontSize = 20.sp,
                    textAlign = TextAlign.End,
                    fontStyle = FontStyle.Italic,
                    color = Emerald
                )
            }
        }
    }

    quoteViewModel.loadQuote()
}