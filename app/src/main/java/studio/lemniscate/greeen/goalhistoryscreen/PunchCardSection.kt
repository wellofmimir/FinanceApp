package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.repositories.GoalRepository
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha


@Composable
fun PunchCardSection(modifier: Modifier = Modifier, onPunchCardFilled: () -> Unit, punchCardSectionViewModel: PunchCardSectionViewModel, tutorialInformation: TutorialInformation) {

    val colors = LocalAppColors.current

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
                .padding(4.dp)
        ) {
            var index = 0

            for (i in 1..5) {

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (j in 1 .. 3) {

                        val filled = index < tokenSoFar

                        Canvas (
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        ) {
                            drawCircle (
                                color = colors.background,
                                style = if (filled) Fill else Stroke(4f)
                            )
                        }

                        ++index
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
                            style = SpanStyle(fontWeight = FontWeight.Bold, color = colors.primary)
                        ) {
                            append("Treat yourself ")
                        }
                        append("once this card is completed.")
                    },
                    fontSize = 16.sp,
                    color = colors.background,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}