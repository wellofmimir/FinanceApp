package studio.lemniscate.greeen.likedquotesscreen

import androidx.compose.foundation.ExperimentalFoundationApi
import studio.lemniscate.greeen.advertisement.AdSectionLargeBanner
import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.homescreen.QuoteViewModel
import studio.lemniscate.greeen.shopscreen.ThemeShopViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppTypography
import studio.lemniscate.greeen.database.Quote
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed

import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LikedQuotesSection (
    quoteViewModel: QuoteViewModel,
    shopViewModel: ThemeShopViewModel,
    tutorialInformation: TutorialInformation
) {

    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    val likedQuotes by quoteViewModel.likedQuotes.collectAsState()
    val adremoverActive by shopViewModel.adRemoverPurchased.collectAsState()

    var menuOpen by remember { mutableStateOf(false) }
    var selectedQuote by remember { mutableStateOf<Quote?>(null) }

    LaunchedEffect(Unit) {
        quoteViewModel.getLikedQuotes()
    }

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(likedQuotes) { index, quote ->

                if (index == 3 || (index % 4 == 0 && index != 0 && index != 4)) {
                    AdSectionLargeBanner (
                        suppressAd = adremoverActive,
                        tutorialInformation = tutorialInformation
                    )
                }

                QuoteCard (
                    quote = quote,
                    typography = typography,
                    colors = colors,
                    onLongPress = {
                        selectedQuote = quote
                        menuOpen = true
                    }
                )
            }

            if (likedQuotes.size <= 3) {
                item {
                    Spacer(Modifier.height(16.dp))

                    AdSectionLargeBanner (
                        suppressAd = adremoverActive,
                        tutorialInformation = tutorialInformation
                    )
                }
            }
        }

        DropdownMenu (
            expanded = menuOpen,
            onDismissRequest = { menuOpen = false }
        ) {
            DropdownMenuItem (
                text = {
                    Text (
                        text = "Delete",
                        color = colors.secondary,
                        fontSize = typography.button
                    )
                },
                onClick = {
                    selectedQuote?.let {
                        quoteViewModel.toggleQuote(it)
                        quoteViewModel.showToast("Quote deleted.")
                    }

                    menuOpen = false
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteCard (
    quote: Quote,
    typography: studio.lemniscate.greeen.ui.theme.AppTypography,
    colors: studio.lemniscate.greeen.ui.theme.AppColors,
    onLongPress: () -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(150.dp, 180.dp)
            .combinedClickable(
                onClick = {},
                onLongClick = onLongPress
            )
            .border(1.dp, colors.secondary, RoundedCornerShape(12.dp))
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
                fontSize = typography.small,
                textAlign = TextAlign.Left,
                color = colors.textPrimary
            )
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .background(colors.surface)
                .padding(horizontal = 12.dp)
        ) {
            Text (
                text = quote.quote,
                fontSize = typography.subtitle,
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
                    shape = RoundedCornerShape (bottomStart = 12.dp, bottomEnd = 12.dp),
                    color = colors.surface
                )
                .padding(end = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text (
                text = quote.name,
                fontSize = typography.medium,
                textAlign = TextAlign.Right,
                fontStyle = FontStyle.Italic,
                color = colors.textPrimary
            )
        }
    }
}