package com.example.financeapp

import android.R
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio

@Composable
fun SinceWhenSection(modifier: Modifier = Modifier, context: Context  = LocalContext.current) {

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        val entries = listOf("Oct", "2 mo.", "6 mo.", "1 yr")

        entries.forEach {
            Box (
                modifier = Modifier
                    .weight(1f)
                    .height(60.dp)
                    .background (
                        color = Pistachio,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = it,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Normal,
                    color = Emerald
                )
            }
        }
    }
}

@Composable
fun AverageSpentSection(modifier: Modifier = Modifier) {

    Box (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text (
            text = "$32,75"
        )
    }
}

@Composable
fun ExpensesOverviewSection(modifier: Modifier = Modifier) {

    Box (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Text (
            text = "This month spent"
        )
    }
}

@Composable
fun ReceiptLogSection(modifier: Modifier = Modifier) {

    Column (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 24.dp, top = 18.dp),
            text = "Receipt log",
            color = Emerald,
            textAlign = TextAlign.Start,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        Column (
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val entries = listOf("Home Depot", "Oct 6.", "$45")

            for (i in 1..5) {

                Row (
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(50.dp)
                ) {

                    entries.forEach {
                        Text (
                            text = it,
                            color = Emerald,
                            fontSize = 22.sp,
                            fontWeight = if (it == entries.last()) FontWeight.Bold else FontWeight.Normal
                            )
                    }
                }
            }
        }
    }
}

