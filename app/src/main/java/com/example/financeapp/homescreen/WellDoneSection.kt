package com.example.financeapp.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import android.content.Context
import com.example.financeapp.R

@Composable
fun WellDoneSection(modifier: Modifier = Modifier, onFinished: () -> Unit, context: Context = LocalContext.current) {

    Column (
        modifier = modifier
            .background(
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            )
            .border (
                width = 2.dp,
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer (
            modifier = Modifier
                .height(50.dp)
        )

        Text (
            text = "Well done!",
            fontSize = 40.sp,
            color = Pistachio
        )

        Spacer (
            modifier = Modifier
                .height(10.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            for (i in 1 .. 3) {
                Image (
                    painter = painterResource(R.drawable.starfilledpistachio_foreground),
                    contentDescription = "Stern_$i",
                    modifier = Modifier
                        .height(50.dp)
                        .aspectRatio(1f)
                )
            }
        }

        Spacer (
            modifier = Modifier
                .height(20.dp)
        )

        Text (
            text = buildAnnotatedString {
                withStyle (
                    style = SpanStyle (
                        color = Pistachio,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append ("You've accomplished a goal!")
                }
            },
            fontSize = 16.sp,
            color = Pistachio,
            textAlign = TextAlign.Center
        )

        Spacer (
            modifier = Modifier
                .height(2.dp)
        )

        Text (
            text = "Now give yourself a little treat. Or a big one. The world is your oyster.",
            fontSize = 16.sp,
            color = Pistachio,
            textAlign = TextAlign.Center
        )

        Spacer (
            modifier = Modifier
                .height(40.dp)
        )

        Box (
            modifier = Modifier
                .background (
                    color = Emerald,
                    shape = RoundedCornerShape(12.dp)
                )
                .border (
                    color = Pistachio,
                    width = 2.dp,
                    shape = RoundedCornerShape(12.dp)
                )
                .height(40.dp)
                .fillMaxWidth(0.9f)
                .align(Alignment.CenterHorizontally)
                .clickable (
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onFinished()
                },
            contentAlignment = Alignment.Center
        ) {
            Text (
                text = "I've treated myself!",
                fontSize = 18.sp,
                color = Pistachio,
                modifier = Modifier
                    .padding(horizontal = 2.dp)
            )
        }
    }
}