package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.badges.BadgeIdentifier

import android.content.Context

import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement

private fun checkIfBadgeIsAvailable (
    currentlyLiked: Boolean,
    numberOfQuotesLiked: Int,
    badgesViewModel: BadgesViewModel
) {
    if (currentlyLiked)
        return

    when (numberOfQuotesLiked) {
        0 -> badgesViewModel.checkBadge(BadgeIdentifier.FIRST_QUOTE_LIKED)
        1 -> badgesViewModel.checkBadge(BadgeIdentifier.SEVEN_QUOTES_LIKED)
        13 -> badgesViewModel.checkBadge(BadgeIdentifier.FOURTEEN_QUOTES_LIKED)
        39 -> badgesViewModel.checkBadge(BadgeIdentifier.FORTY_QUOTES_LIKED)
        69 -> badgesViewModel.checkBadge(BadgeIdentifier.SEVENTY_QUOTES_LIKED)
        99 -> badgesViewModel.checkBadge(BadgeIdentifier.HUNDRED_QUOTES_LIKED)
        129 -> badgesViewModel.checkBadge(BadgeIdentifier.ONE_THIRTY_QUOTES_LIKED)
        189 -> badgesViewModel.checkBadge(BadgeIdentifier.ONE_NINETY_QUOTES_LIKED)
        359 -> badgesViewModel.checkBadge(BadgeIdentifier.THREE_SIXTY_QUOTES_LIKED)
    }
}

@Composable
fun QuoteSection (
    modifier: Modifier = Modifier,
    quoteViewModel: QuoteViewModel,
    badgesViewModel: BadgesViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current

    val quote by quoteViewModel.quote.collectAsState()
    val quoteLiked by quoteViewModel.quoteLiked.collectAsState()
    val isLoading by quoteViewModel.isLoading.collectAsState()
    val likedQuotes by quoteViewModel.likedQuotes.collectAsState()
    val numberOfQuotesLiked = likedQuotes.size

    LaunchedEffect(Unit) {
        quoteViewModel.loadQuoteWithDelay()
    }

    Column (
        modifier = modifier
            .aspectRatio(1f)
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_QUOTE) 0.1f else 1.0f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background (
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                    color = colors.surface
                )
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Box (
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = quote.quote,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.ExtraBold,
                    color = colors.textPrimary
                )
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background (
                    shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = colors.surface
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(start = 2.dp)
                    .size(64.dp)
            ) {
                Image (
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "HerzZumLiken",
                    colorFilter = ColorFilter.tint(if (quoteLiked) Color.Red else colors.textPrimary),
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ){
                            if (isLoading)
                                return@clickable

                            if (quoteViewModel.hasError())
                                return@clickable

                            quoteViewModel.toggleQuote(quote)

                            checkIfBadgeIsAvailable (
                                currentlyLiked = quoteLiked,
                                numberOfQuotesLiked = numberOfQuotesLiked,
                                badgesViewModel = badgesViewModel
                            )
                        }
                )
            }

            Box (
                modifier = Modifier
                    .padding(end = 12.dp, bottom = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = quote.name,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    color = colors.textPrimary
                )
            }
        }
    }
}