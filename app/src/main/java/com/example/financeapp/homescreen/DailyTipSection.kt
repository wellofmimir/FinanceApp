package com.example.financeapp.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import android.text.Layout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.ui.theme.Emerald


@Composable
fun DailyTipSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    Row (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = Emerald,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box (
            modifier = Modifier
                .aspectRatio(1f)
                .background (
                    color = Pistachio,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Box (
                modifier = Modifier
                    .size(64.dp)
                    .background (
                        color = Emerald,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image (
                    modifier = Modifier
                        .size(48.dp),
                    painter = painterResource(R.drawable.dollarsign_foreground),
                    contentDescription = "Dollarzeichen",
                    alignment = Alignment.Center
                )
            }
        }

        Spacer (
            modifier = Modifier
                .width(4.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxHeight(1f)
                .weight(2f)
                .background (
                    color = Pistachio,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text (
                text = "Tap here to see your financial tip",
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}