package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.receiptsscreen.ReceiptSectionsViewModel
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.material3.Text

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState


@Composable
fun SavedReceiptsSection (
    modifier: Modifier = Modifier,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val receiptsThisMonth = receiptSectionsViewModel.receipts.collectAsState()

    LaunchedEffect(Unit) {
        receiptSectionsViewModel.getReceiptsForACertainTimespan(receiptSectionsViewModel.getFirstDayOfCurrentMonth(), receiptSectionsViewModel.getLastDayOfCurrentMonth())
    }

    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_SAVED_RECEIPTS) 0.1f else 1.0f)
            .fillMaxWidth()
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.spacedBy (
            space = 0.dp,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val fontSize = when (receiptsThisMonth.value.size) {
            in 0..9 -> 72.sp
            in 9 .. 99 -> 72.sp
            in 100 .. 999 -> 72.sp
            in 1000 .. 9999 -> 60.sp
            in 10000 .. 99999 -> 52.sp
            in 100000 .. 999999 -> 44.sp
            else -> 36.sp
        }

        Text (
            text = receiptsThisMonth.value.size.toString(),
            fontSize = fontSize,
            color = colors.textPrimary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(3f)
        )

        Text (
            text = "Saved Receipts",
            fontSize = 20.sp,
            color = colors.textPrimary,
            modifier = Modifier
                .weight(1f)
        )

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )
    }
}
