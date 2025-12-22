package com.example.financeapp.likedquotes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.QuoteRepository
import com.example.financeapp.TutorialInformation
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun LikedQuotesSection(tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    val likedQuotesSectionViewModel: LikedQuotesSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val repository = QuoteRepository.Companion.getInstance(database)

                return LikedQuotesSectionViewModel(repository) as T
            }
        }
    )

    var likedQuotes = likedQuotesSectionViewModel.getLikedQuotes()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background (
                colors.background
            )
    ) {
        Column (
            modifier = Modifier
                .background (
                    color = colors.background
                )
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            likedQuotes.take(likedQuotes.size).forEachIndexed { index, quote ->

                if (index == 3 || index % 4 == 0 && index != 0 && index != 4) { // am Anfang ist die vierte Kachel eine Werbung(index = 3) -> deswegen wird Index=4 ausgeschlossen!! -> danach soll jede Dritte Kachel soll eine Werbung sein
                    AdSectionLargeBanner (
                        tutorialInformation = tutorialInformation
                    )

                    Spacer (
                        modifier = Modifier
                            .padding(1.dp)
                    )
                }

                Column (
                    modifier = Modifier
                        .height(180.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f)
                            .background (
                                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                                color = colors.surface
                            )
                            .padding(top = 12.dp, start = 12.dp)
                    ) {
                        Text (
                            text = quote.date,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Left,
                            color = colors.textPrimary
                        )
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f)
                            .background (
                                color = colors.surface
                            )
                            .padding(start = 12.dp)
                    ) {
                        Text (
                            text = quote.quote,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.ExtraBold,
                            color = colors.textPrimary
                        )
                    }

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.3f)
                            .background (
                                shape = RoundedCornerShape(
                                    bottomStart = 12.dp,
                                    bottomEnd = 12.dp
                                ),
                                color = colors.surface
                            )
                            .padding(end = 12.dp, bottom = 12.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text (
                            text = quote.name,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Right,
                            fontStyle = FontStyle.Italic,
                            color = colors.textPrimary
                        )
                    }
                }

                Spacer(
                    modifier = Modifier
                        .padding(2.dp)
                )
            }

            if (likedQuotes.size <= 3) {

                Box (
                    modifier = Modifier
                        .fillMaxSize()
                        .background (
                            color = colors.background
                        )
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer (
                            modifier = Modifier
                                .height(2.dp)
                        )

                        AdSectionLargeBanner (
                            tutorialInformation = tutorialInformation
                        )
                    }
                }
            }
        }
    }
}