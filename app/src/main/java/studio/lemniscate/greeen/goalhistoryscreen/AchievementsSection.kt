package studio.lemniscate.greeen.goalhistoryscreen

import studio.lemniscate.greeen.database.Goal
import studio.lemniscate.greeen.R
import studio.lemniscate.greeen.homescreen.TutorialInformation
import studio.lemniscate.greeen.TutorialStep
import studio.lemniscate.greeen.commonutils.fixOrientation
import studio.lemniscate.greeen.commonutils.getShareableImageUri
import studio.lemniscate.greeen.commonutils.shareToFacebook
import studio.lemniscate.greeen.commonutils.shareToFacebookMessenger
import studio.lemniscate.greeen.commonutils.shareToWhatsapp
import studio.lemniscate.greeen.homescreen.AchievementsSectionViewModel
import studio.lemniscate.greeen.homescreen.ShareAchievementEvent
import studio.lemniscate.greeen.ui.theme.LocalAppColors


import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text

import android.content.Context

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import java.io.File

@Composable
fun AchievementsSection (
    modifier: Modifier = Modifier,
    achievementsSectionViewModel: AchievementsSectionViewModel,
    tutorialInformation: TutorialInformation,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    val goals by achievementsSectionViewModel.goals.collectAsState()
    achievementsSectionViewModel.getCompletedGoals(tutorialInformation.isActive)

    var showDialog by remember { mutableStateOf(false) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var openedGoal by remember { mutableStateOf<Goal?>(null) }

    var whatsAppClicked by remember { mutableStateOf(false) }
    var facebookClicked by remember { mutableStateOf(false) }
    var facebookMessengerClicked by remember {mutableStateOf(false)}

    LaunchedEffect(whatsAppClicked) {
        achievementsSectionViewModel.shareEventForWhatsApp.collect { event ->
            when (event) {
                is ShareAchievementEvent.SharedAchievement -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToWhatsapp(context, uri, event.text)
                    whatsAppClicked = false
                }
            }
        }
    }

    LaunchedEffect(facebookClicked) {
        achievementsSectionViewModel.shareEventForFacebook.collect { event ->
            when (event) {
                is ShareAchievementEvent.SharedAchievement -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToFacebook(context, uri)
                    facebookClicked = false
                }
            }
        }
    }

    LaunchedEffect(facebookMessengerClicked) {
        achievementsSectionViewModel.shareEventForFacebookMessenger.collect { event ->
            when (event) {
                is ShareAchievementEvent.SharedAchievement -> {
                    val uri = getShareableImageUri(context, event.imageUri.toString())
                    shareToFacebookMessenger(context, uri)
                    facebookMessengerClicked = false
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
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image (
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f)
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

                Spacer (
                    modifier = Modifier
                        .padding(4.dp)
                )

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .border (
                            width = 1.dp,
                            shape = RoundedCornerShape(12.dp),
                            color = colors.secondary
                        )
                        .background (
                            color = colors.background,
                            shape = RoundedCornerShape(12.dp)
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
                            text = openedGoal?.goal.toString(),
                            color = colors.secondary,
                            fontSize = 24.sp
                        )

                        Spacer (
                            modifier = Modifier
                                .padding(4.dp)
                        )

                        Text (
                            text = "Share",
                            color = colors.secondary
                        )

                        Spacer (
                            modifier = Modifier
                                .height(6.dp)
                        )

                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .size(200.dp)
                        ) {
                            Box (
                                modifier = Modifier
                                    .weight(1f)
                                    .size(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image (
                                    painter = painterResource(R.drawable.whatsapp_foreground),
                                    contentDescription = "Whatsapp-Logo",
                                    colorFilter = ColorFilter.tint(colors.secondary),
                                    modifier = Modifier
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                return@clickable

                                            openedGoal?.let {
                                                achievementsSectionViewModel.shareAchievementOnWhatsApp(it)
                                                whatsAppClicked = true
                                            }
                                        }
                                )
                            }

                            Box (
                                modifier = Modifier
                                    .weight(1f)
                                    .size(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image (
                                    painter = painterResource(R.drawable.facebook_foreground),
                                    contentDescription = "Facebook-Logo",
                                    colorFilter = ColorFilter.tint(colors.secondary),
                                    modifier = Modifier
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                return@clickable

                                            openedGoal?.let {
                                                achievementsSectionViewModel.shareAchievementOnFacebook(it)
                                                facebookClicked = true
                                            }
                                        }
                                )
                            }

                            Box (
                                modifier = Modifier
                                    .weight(1f)
                                    .size(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image (
                                    painter = painterResource(R.drawable.facebookmessenger_foreground),
                                    contentDescription = "Facebook-Messenger",
                                    colorFilter = ColorFilter.tint(colors.secondary),
                                    modifier = Modifier
                                        .clickable (
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            if (whatsAppClicked || facebookClicked || facebookMessengerClicked)
                                                return@clickable

                                            openedGoal?.let {
                                                achievementsSectionViewModel.shareAchievementOnFacebookMessenger(it)
                                                facebookMessengerClicked = true
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    Column (
        modifier = modifier
            .alpha(if (tutorialInformation.isActive && tutorialInformation.tutorialStep != TutorialStep.GOALS_ACHIEVEMENTS) 0.1f else 1.0f)
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text (
                text = "What You've Achieved:",
                color = colors.primary,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .background (
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                        color = colors.secondary
                    )
                    .padding(start = 24.dp, top = 24.dp, end = 0.dp, bottom = 24.dp)
            )
        }

        val listState = rememberLazyListState()

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background (
                    color = colors.secondary,
                    shape = RoundedCornerShape(12.dp)
                )
                .weight(1f),
            state = listState
        ) {
            items(goals.take(goals.size)) {goal ->

                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource()}
                        ) {
                            val file = File(goal.pathToImage)

                            if (file.exists()) {
                                bitmap = BitmapFactory
                                    .decodeFile(file.absolutePath)
                                    .fixOrientation(file.absolutePath)

                                openedGoal = goal
                                showDialog = true
                            }
                        }
                ) {
                    Text (
                        text = goal.goal,
                        color = colors.primary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(start = 40.dp, top = 12.dp, end = 0.dp, bottom = 0.dp)
                            .weight(1f)
                    )

                    Text (
                        text = goal.dateWhenFinished,
                        color = colors.primary,
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