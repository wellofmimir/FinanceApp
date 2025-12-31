package com.example.financeapp.goalhistoryscreen

import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.financeapp.commonutils.fixOrientation
import com.example.financeapp.homescreen.AchievementsSectionViewModel
import com.example.financeapp.ui.theme.LocalAppColors
import java.io.File

@Composable
fun RandomMemoryPictureSection(modifier: Modifier = Modifier, achievementsSectionViewModel: AchievementsSectionViewModel) {

    val colors = LocalAppColors.current
    val goals by achievementsSectionViewModel.goalsOrderedRandomly.collectAsState()
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        achievementsSectionViewModel.getCompletedGoalsOrderedRandomly()
    }

    val goal = goals.firstOrNull()

    LaunchedEffect(goal) {
        goal?.let {
            if (goals.isNotEmpty()) {
                val file = File(goals.first().pathToImage)

                if (file.exists()) {
                    bitmap = BitmapFactory
                        .decodeFile(file.absolutePath)
                        .fixOrientation(file.absolutePath)
                }
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
            Image(
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
                            achievementsSectionViewModel.getCompletedGoalsOrderedRandomly()
                        }
                    }
            )
        }
    }

    Box (
        modifier = modifier
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
                textAlign = TextAlign.Center
            )
    }
}