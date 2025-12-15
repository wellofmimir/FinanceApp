package com.example.financeapp.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle

import com.example.financeapp.R
import com.example.financeapp.receiptsscreen.ReceiptSectionsViewModel
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep

@Composable
fun SavedReceiptsSection(modifier: Modifier = Modifier, receiptSectionsViewModel: ReceiptSectionsViewModel, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
    val receiptsThisMonth = receiptSectionsViewModel.receipts.collectAsState()

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_SAVED_RECEIPTS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background(
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text (
                text = "This Month:",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                color = Emerald,
                modifier = Modifier
                    .padding(start = 14.dp, top = 18.dp)
                    .weight(2f)
            )

            Image (
                painter = painterResource(R.drawable.receipt_foreground),
                contentDescription = "ReceiptLogo",
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(64.dp)
            )
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(start = 14.dp)
        ) {
            Text (
                text = receiptsThisMonth.value.size.toString(),
                fontSize = 84.sp,
                color = Emerald,
                fontWeight = FontWeight.Bold
            )

            Text (
                text = "Saved Receipts",
                fontSize = 20.sp,
                color = Emerald
            )
        }
    }
}
