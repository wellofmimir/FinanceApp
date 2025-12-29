package com.example.financeapp.dailytipscreen
import com.example.financeapp.R

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.ui.theme.LocalAppColors

@Composable
fun RandomTipSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Random favourite tip:",
            fontSize = 16.sp,
            color = colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )

        Spacer (
            modifier = Modifier
                .padding(2.dp)
        )

        Text (
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt",
            fontSize = 14.sp,
            color = colors.primary,
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
        )

        Spacer (
            modifier = Modifier
                .padding(12.dp)
        )

        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box (
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Image (
                    painter = painterResource(R.drawable.herzzumliken_foreground),
                    contentDescription = "Herz"
                )
            }

            Spacer (
                modifier = Modifier
                    .padding(horizontal = 6.dp)
            )

            Box (
                modifier = Modifier
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = "Tip no. 11234",
                    color = colors.primary
                )
            }
        }
    }
}