package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.dailytipscreen.DailyTipScreenViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState

import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier

import androidx.compose.material3.Text

@Composable
fun DailyTipSection (
    modifier: Modifier = Modifier,
    tutorialInformation: TutorialInformation,
    dailyTipScreenViewModel: DailyTipScreenViewModel,
    dailyTipSectionClicked: () -> Unit
) {
    val colors = LocalAppColors.current
    val newDailyTipAvailable by dailyTipScreenViewModel.newDailyTipAvailable.collectAsState()

    val buttonText = if (newDailyTipAvailable)
        "Your daily finance tip is ready! Hooray!"
    else
        "See your finance tips."

    Box (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.HOMESCREEN_DAILY_FINANCIAL_TIP) 0.1f else 1.0f)
            .clickable {
                if (tutorialInformation.isActive)
                    return@clickable

                dailyTipSectionClicked()
            }
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text (
            text = buttonText,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = colors.primary,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        )
    }
}


