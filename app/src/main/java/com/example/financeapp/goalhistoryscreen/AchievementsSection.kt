package com.example.financeapp.goalhistoryscreen
import com.example.financeapp.database.Goal

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.financeapp.R
import com.example.financeapp.TutorialInformation
import com.example.financeapp.TutorialStep
import com.example.financeapp.commonutils.fixOrientation
import com.example.financeapp.commonutils.getShareableImageUri
import com.example.financeapp.commonutils.shareToWhatsapp
import com.example.financeapp.homescreen.AchievementsSectionViewModel
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.homescreen.ShareAchievementEvent
import com.example.financeapp.receiptsscreen.ShareEvent
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.ui.theme.LocalAppColors
import java.io.File

@Composable
fun AchievementsSection(modifier: Modifier = Modifier, tutorialInformation: TutorialInformation, context: Context = LocalContext.current) {

    val colors = LocalAppColors.current

    val achievementsSectionViewModel: AchievementsSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun<T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val repository = GoalRepository(database)

                return AchievementsSectionViewModel(repository) as T
            }
        }
    )

    val goals by achievementsSectionViewModel.goals.collectAsState()
    achievementsSectionViewModel.getCompletedGoals()

    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var openedGoal by remember { mutableStateOf<Goal?>(null) }

    LaunchedEffect(Unit) {
        achievementsSectionViewModel.shareEvent.collect { event ->
            when (event) {
                is ShareAchievementEvent.SharedAchievement -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToWhatsapp(context, uri, event.text)
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
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image (
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(5f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            if (showDialog) {
                                showDialog = false
                            }
                        }
                )

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                        .border (
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = colors.secondary
                        )
                        .background (
                            color = colors.background
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 12.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text (
                            text = "Share",
                            color = colors.textSecondary
                        )

                        Spacer (
                            modifier = Modifier
                                .height(12.dp)
                        )

                        Image (
                            painter = painterResource(R.drawable.whatsapplogo_foreground),
                            contentDescription = "Whatsapp-Logo",
                            colorFilter = ColorFilter.tint(colors.secondary),
                            modifier = Modifier
                                .size(26.dp)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    openedGoal?.let {
                                        achievementsSectionViewModel.shareAchievement(it)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }


    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_ACHIEVEMENTS) 0.1f else 1.0f)
            .background (
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "What You've Achieved:",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = colors.surface
                    )
                    .padding(start = 24.dp, top = 24.dp, end = 0.dp, bottom = 24.dp)
            )
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = colors.surface,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            state = listState
        ) {
            items(goals.take(goals.size)) {goal ->

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource()}
                        ) {
                            val file = File(goal.pathToImage)

                            if (file.exists()) {
                                bitmap = BitmapFactory.decodeFile(file.absolutePath).fixOrientation(file.absolutePath)
                                openedGoal = goal
                                showDialog = true
                            }
                        }
                ) {
                    Text (
                        text = goal.goal,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 40.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
                            .weight(1f)
                    )

                    Text (
                        text = goal.dateWhenFinished,
                        color = colors.textPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
                            .weight(1f)
                    )
                }
            }
        }

    }
}