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

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable

import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun SavedReceiptsSection (
    modifier: Modifier = Modifier,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val receiptsThisMonth = receiptSectionsViewModel.receipts.collectAsStateWithLifecycle()

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text (
                text = "This Month:",
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic,
                color = colors.primary,
                modifier = Modifier
                    .padding(start = 8.dp, top = 16.dp)
            )

            Box (
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background (
                        color = colors.background,
                        shape = CircleShape
                    )
                    .border (
                        width = 1.dp,
                        color = colors.background,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image (
                    painter = painterResource(R.drawable.receiptsymbol_foreground),
                    contentDescription = "ReceiptLogo",
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 4.dp, start = 4.dp)
                        .background (
                            color = colors.background,
                            shape = CircleShape
                        ),
                    colorFilter = ColorFilter.tint(colors.surface),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.BottomEnd
                )
            }
        }

        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(2f)
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
                fontWeight = FontWeight.Bold
            )

            Text (
                text = "Saved Receipts",
                fontSize = 20.sp,
                color = colors.textPrimary
            )
        }
    }
}
