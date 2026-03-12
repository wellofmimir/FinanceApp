package studio.lemniscate.greeen.homescreen

import androidx.compose.foundation.Image
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.receiptsscreen.ReceiptSectionsViewModel
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.Emerald

import androidx.compose.material3.Text

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import studio.lemniscate.greeen.ui.theme.LocalAppTypography
import studio.lemniscate.greeen.ui.theme.NormalTypography
import studio.lemniscate.greeen.ui.theme.SmallTypography

@Composable
fun SavedReceiptsSection (
    modifier: Modifier = Modifier,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current
    val receiptsThisMonth by receiptSectionsViewModel.receipts.collectAsState()

    if (typography == NormalTypography || typography == SmallTypography) {
        val fontSizeMultiplicator = when (receiptsThisMonth.size) {
            in 0..9 -> 3f
            in 10..99 -> 3f
            in 100..999 -> 2f
            in 1000..9999 -> 1.5f
            in 10000..99999 ->1f
            else -> 0.75f
        }

        Box (
            modifier = modifier
                .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_SAVED_RECEIPTS) 0.1f else 1.0f)
                .fillMaxWidth()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(start = 12.dp, top = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text (
                text = buildAnnotatedString {
                    withStyle (
                        SpanStyle (
                            fontSize = typography.title * fontSizeMultiplicator,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    ) {
                        append(receiptsThisMonth.size.toString())
                    }

                    append("\n")

                    withStyle (
                        SpanStyle (
                            fontSize = typography.medium,
                            color = colors.primary
                        )
                    ) {
                        append("Saved Receipts")
                    }
                },
                textAlign = TextAlign.Center,
            )
        }

        return
    }

    val fontSizeMultiplicator = when (receiptsThisMonth.size) {
        in 0..9 -> 2.5f
        in 10..99 -> 2f
        in 100..999 -> 1.5f
        in 1000..9999 -> 1f
        in 10000..99999 ->0.75f
        else -> 0.75f
    }

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
            )
            .padding(start = 12.dp, top = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .weight(2.5f)
                .fillMaxWidth()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                modifier = Modifier
                    .weight(4f),
                text = "This Month:",
                fontSize = typography.button,
                color = colors.primary
            )

            Box (
                modifier = Modifier
                    .weight(2f)
                    .padding(top = 2.dp, end = 8.dp)
                    .sizeIn(32.dp, 32.dp,64.dp, 64.dp)
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
                        .padding(top = 8.dp, start = 6.dp)
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

        Box (
            modifier = Modifier
                .weight(3f),
            contentAlignment = Alignment.BottomStart
        ) {
            Text (
                text = buildAnnotatedString {
                    withStyle (
                        SpanStyle (
                            fontSize = typography.title * fontSizeMultiplicator,
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    ) {
                        append(receiptsThisMonth.size.toString())
                    }

                    append("\n")

                    withStyle (
                        SpanStyle (
                            fontSize = typography.medium,
                            color = colors.primary
                        )
                    ) {
                        append("Saved Receipts")
                    }
                },
                textAlign = TextAlign.Start,
            )
        }

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )
    }
}
