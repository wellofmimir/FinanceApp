package com.example.financeapp.likedquotesscreen
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.financeapp.advertisement.AdSectionLargeBanner
import com.example.financeapp.TutorialInformation
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.financeapp.advertisement.AdvertisementViewModel
import com.example.financeapp.homescreen.QuoteViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikedQuotesSection (
    quoteViewModel: QuoteViewModel,
    advertisementViewModel: AdvertisementViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    val likedQuotes by quoteViewModel.likedQuotes.collectAsState()
    var menuOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        quoteViewModel.getLikedQuotes()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background (
                colors.background
            )
            .combinedClickable (
                onClick = {},
                onLongClick = {
                    menuOpen = true
                }
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

                DropdownMenu (
                    modifier = Modifier
                        .border (
                            width = 1.dp,
                            color = colors.secondary
                        )
                        .background (
                            color = colors.primary
                        ),
                    expanded = menuOpen,
                    onDismissRequest = {
                        menuOpen = false
                    }
                ) {
                    DropdownMenuItem (
                        modifier = Modifier
                            .background (
                                color = colors.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        text = {
                            Text (
                                text = "Delete",
                                color = colors.secondary
                            )
                        },
                        onClick = {
                            menuOpen = false
                            quoteViewModel.toggleQuote(quote)
                            quoteViewModel.showToast("Quote deleted.")
                        }
                    )
                }

                if (index == 3 || index % 4 == 0 && index != 0 && index != 4) { // am Anfang ist die vierte Kachel eine Werbung(index = 3) -> deswegen wird Index=4 ausgeschlossen!! -> danach soll jede Dritte Kachel soll eine Werbung sein

                    AdSectionLargeBanner (
                        supressAd = advertisementViewModel.getRemoveAllAds(),
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
                                shape = RoundedCornerShape (
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
                            supressAd = advertisementViewModel.getRemoveAllAds(),
                            tutorialInformation = tutorialInformation
                        )
                    }
                }
            }
        }
    }
}