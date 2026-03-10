package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.badges.BadgesViewModel
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration

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
    }
}

@Composable
fun QuoteSection (
    modifier: Modifier = Modifier,
    quoteViewModel: QuoteViewModel,
    badgesViewModel: BadgesViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val configuration = LocalConfiguration.current
    val typography = LocalAppTypography.current

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
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_QUOTE) 0.1f else 1.0f)
            .aspectRatio(1f)
            .fillMaxSize()
            .background (
                shape = RoundedCornerShape(12.dp),
                color = colors.secondary
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val verticalScroll = rememberScrollState()

        Text (
            text = quote.quote,
            fontSize = typography.body,
            textAlign = TextAlign.Left,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.ExtraBold,
            color = colors.primary,
            modifier = Modifier
                .weight(4f)
                .verticalScroll(verticalScroll)
                .padding(start = 16.dp, top = 12.dp, end = 16.dp)
        )

        Row (
            modifier = Modifier
                .weight(1.5f)
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
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image (
                painter = painterResource(R.drawable.herzzumliken_foreground),
                contentDescription = "HerzZumLiken",
                colorFilter = ColorFilter.tint(if (quoteLiked) Color.Red else colors.textPrimary),
                modifier = Modifier
                    .size(44.dp)
                    .padding(start = 8.dp)
                    .weight(1f)
            )

            Text (
                text = quote.name,
                fontSize = typography.small,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic,
                color = colors.primary,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(3f)
            )
        }
    }
}