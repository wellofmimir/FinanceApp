package com.example.financeapp

import android.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio

@Composable
fun PunchCardSection(modifier: Modifier = Modifier) {

    Column (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        for (i in 1..5) {

            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = Emerald,
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = Emerald,
                        style = Fill
                    )
                }

                Canvas (
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    drawCircle (
                        color = Emerald,
                        style = Fill
                    )
                }
            }
        }

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 4.dp)
        ) {
            Spacer (
                modifier = Modifier
                    .padding(4.dp)
            )

            Text (
                text = buildAnnotatedString {
                    withStyle (
                        style = SpanStyle(fontWeight = FontWeight.Bold)
                    ) {
                        append("Treat yourself ")
                    }
                    append("once this card is completed.")
                },
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}