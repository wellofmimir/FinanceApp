package com.example.financeapp.homescreen
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun QuoteSection(modifier: Modifier = Modifier, quoteViewModel: QuoteViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current
    val quote by quoteViewModel.quote.collectAsState()
    val quoteLiked by quoteViewModel.quoteLiked.collectAsState()
    val isLoading by quoteViewModel.isLoading.collectAsState()

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

                            quoteViewModel.quoteGotLiked(quote)
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