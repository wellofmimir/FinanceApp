package com.example.financeapp

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.financeapp.ui.theme.Emerald


@Composable
fun LikedQuotesSection(context: Context = LocalContext.current) {

    val likedQuotesSectionViewModel: LikedQuotesSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.getInstance(context)
                val repository = QuoteRepository(database)

                return LikedQuotesSectionViewModel(repository) as T
            }
        }
    )

    val likedQuotes = likedQuotesSectionViewModel.getLikedQuotes()

    Column (
        modifier = Modifier
            .background(
                color = Emerald
            ),
        verticalArrangement = Arrangement.Center
    ) {

        likedQuotes.take(likedQuotes.size).forEach { quote ->

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
                            color = Pistachio
                        )
                        .padding(top = 12.dp, start = 12.dp)
                ) {
                    Text (
                        text = "October 6, 2025",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f)
                        .background (
                            color = Pistachio
                        )
                        .padding(start = 12.dp)
                ) {
                    Text (
                        text = quote.quote,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.3f)
                        .background (
                            shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                            color = Pistachio
                        )
                        .padding(end = 12.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text (
                        text = quote.name,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Right,
                        fontStyle = FontStyle.Italic
                    )
                }
            }

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )
        }
    }

}