package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState


import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Color

import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

@Composable
fun PunchCardSection (
    modifier: Modifier = Modifier,
    onPunchCardFilled: () -> Unit,
    punchCardSectionViewModel: PunchCardSectionViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    LaunchedEffect(Unit) {
        punchCardSectionViewModel.getTokenSoFarForPunchcard()
    }

    val tokenSoFar by punchCardSectionViewModel.tokenSoFar.collectAsState()
    val isPunchCardFull = tokenSoFar >= 15

    if (isPunchCardFull) { //15 token sind in der punchCardSection zu sehen
        val spareToken = tokenSoFar - 15
        punchCardSectionViewModel.resetTokenSoFarForPunchcard(spareToken = spareToken)
        punchCardSectionViewModel.getTokenSoFarForPunchcard()

        onPunchCardFilled()
    } else {
        Column (
            modifier = modifier
                .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_PUNCHCARD) 0.1f else 1.0f)
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(4.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            var index = 0

            Spacer (
                modifier = Modifier
                    .padding(4.dp)
            )

            repeat(5) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) {
                        val filled = index < tokenSoFar

                        Image (
                            modifier = Modifier
                                .background (
                                    shape = CircleShape,
                                    color = Color.Transparent
                                )
                                .border (
                                    width = if (filled) 0.dp else 1.dp,
                                    shape = CircleShape,
                                    color = if (filled) colors.secondary else colors.primary
                                )
                                .clip(CircleShape),
                            painter = painterResource(R.drawable.ringmitduennemrand_foreground),
                            colorFilter = ColorFilter.tint(if (filled) colors.primary else colors.secondary),
                            contentDescription = "RingBild"
                        )

                        ++index
                    }

                }
            }

            Spacer (
                modifier = Modifier
                    .padding(2.dp)
            )

            Text (
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = buildAnnotatedString {
                    withStyle (
                        style = SpanStyle (
                            fontWeight = FontWeight.Bold,
                            color = colors.primary
                        )
                    ) {
                        append("Treat yourself ")
                    }
                    append("once this card is completed.")
                },
                fontSize = typography.small,
                color = colors.background,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}