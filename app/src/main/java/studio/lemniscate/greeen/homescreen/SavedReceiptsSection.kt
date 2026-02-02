package studio.lemniscate.greeen.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale

import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.receiptsscreen.ReceiptSectionsViewModel
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

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
                color = colors.textPrimary,
                modifier = Modifier
                    .padding(start = 14.dp, top = 18.dp)
                    .weight(2f)
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(start = 14.dp)
        ) {
            Text (
                text = receiptsThisMonth.value.size.toString(),
                fontSize = 84.sp,
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
