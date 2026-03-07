package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.commonutils.fixOrientation
import studio.lemniscate.greeen.homescreen.AchievementsSectionViewModel
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.homescreen.TutorialInformation

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.unit.sp

import java.io.File

@Composable
fun RandomMemoryPictureSection (
    modifier: Modifier = Modifier,
    achievementsSectionViewModel: AchievementsSectionViewModel,
    tutorialInformation: TutorialInformation
) {
    val colors = LocalAppColors.current
    val firstGoal by achievementsSectionViewModel.randomFirstGoal.collectAsState()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        achievementsSectionViewModel.getCompletedGoalsOrderedRandomly()
        achievementsSectionViewModel.startGoalRotation(5000)
    }

    DisposableEffect(Unit) {
        onDispose {
            achievementsSectionViewModel.stopGoalRotation()
        }
    }

    LaunchedEffect(firstGoal) {
        firstGoal?.let {
            val file = File(it.pathToImage)

            if (file.exists()) {
                val newBitmap = BitmapFactory
                    .decodeFile(file.absolutePath)
                    .fixOrientation(file.absolutePath)

                bitmap?.let {
                    if (newBitmap == bitmap) {
                        achievementsSectionViewModel.getCompletedGoalsOrderedRandomly()
                        return@LaunchedEffect
                    }
                }

                bitmap = newBitmap
            }
        }
    }


    if (showDialog && bitmap != null) {
        Dialog (
            onDismissRequest = {
                showDialog = false
            },
            properties = DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            Image (
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip (
                        shape =RoundedCornerShape(12.dp)
                    )
                    .clickable (
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        if (showDialog) {
                            showDialog = false
                            achievementsSectionViewModel.startGoalRotation(5000)
                        }
                    }
            )
        }
    }

    Box (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.RECEIPTS_RANDOM_MEMORY) 0.1f else 1.0f)
            .fillMaxSize()
            .background (
                color = colors.background,
                shape = RoundedCornerShape(12.dp)
            )
            .border (
                color = colors.surface,
                width = 4.dp,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable (
            ) {
                if (tutorialInformation.isActive)
                    return@clickable

                achievementsSectionViewModel.stopGoalRotation()
                showDialog = true
            },
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image (
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        } ?: Text (
                text = "Create memories by achieving your goals!",
                color = colors.secondary,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(8.dp)
            )
    }
}