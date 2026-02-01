package com.example.financeapp.dailytipscreen

import com.example.financeapp.badges.BadgesViewModel
import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.database.Badge
import com.example.financeapp.commonutils.setWallpaper

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import com.example.financeapp.commonutils.fixOrientation
import com.example.financeapp.commonutils.setWallpaperWithChooser

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BadgesSection (
    modifier: Modifier = Modifier,
    badgesViewModel: BadgesViewModel,
    onDismissRequest: () -> Unit,
    context: Context = LocalContext.current
) {
    val colors = LocalAppColors.current
    val scrollState = rememberScrollState()
    val userBadges by badgesViewModel.userBadges.collectAsState()

    var showWallpaper by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var temporaryBadge by remember { mutableStateOf<Badge?>(null) }

    LaunchedEffect(Unit) {
        badgesViewModel.loadUserBadges()
    }

    if (showWallpaper && temporaryBadge != null) {
        Dialog (
            onDismissRequest = {
                showWallpaper = false
                temporaryBadge = null
            },
            properties = DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            val imageBitmapFromTemporaryBadge = badgesViewModel.getImageBitmapFromBadge(temporaryBadge!!)

            if (imageBitmapFromTemporaryBadge == null) {
                AlertDialog (
                    modifier = Modifier
                        .background (
                            color = colors.primary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .height(250.dp),
                    onDismissRequest = {
                        showWallpaper = false
                        temporaryBadge = null
                    },
                    title = {
                        Text (
                            text = "An error has occurred.",
                            color = colors.secondary
                        )
                    },
                    text = {
                        Text (
                            text = "Unfortunately the wallpaper could not be loaded.\nPlease write an eMail to our support: greeen.development.team@gmail.com",
                            color = colors.secondary
                        )
                    },
                    confirmButton = {
                        TextButton (
                            modifier = Modifier
                                .border (
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background (
                                    color = colors.secondary,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            onClick = {
                                showWallpaper = false
                                temporaryBadge = null
                            }
                        ) {
                            Text (
                                text = "Okay",
                                color = colors.primary
                            )
                        }
                    },
                    containerColor = colors.primary
                )

                return@Dialog
            }

            DropdownMenu (
                modifier = Modifier
                    .border (
                        width = 1.dp,
                        color = colors.secondary
                    )
                    .background (
                        color = colors.primary
                    ),
                expanded = showMenu,
                onDismissRequest = {
                    showMenu = false
                }
            ) {
                DropdownMenuItem (
                    modifier = Modifier
                        .background (
                            color = colors.primary,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    text = {
                        Text (
                            text = "Set As Wallpaper",
                            color = colors.secondary
                        )
                    },
                    onClick = {
                        showMenu = false
                        setWallpaperWithChooser(context, BitmapFactory.decodeFile(temporaryBadge!!.pathToImage).fixOrientation(temporaryBadge!!.pathToImage))
                    }
                )
            }

            Image (
                bitmap = imageBitmapFromTemporaryBadge,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .combinedClickable (
                        onClick = {
                            showWallpaper = false
                        },
                        onLongClick = {
                            showMenu = true
                        }
                    )
            )
        }
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable() {
                onDismissRequest()
            }
            .padding(12.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userBadges.forEach { badge ->
            if (!badge.isGranted)
                return@forEach

            BadgeTile (
                title = badge.title,
                text = badge.text,
                onSeeGift = {
                    if (badge.pathToImage.isNotEmpty())
                        showWallpaper = true

                    temporaryBadge = badge
                },
                showGiftText = badge.pathToImage.isNotEmpty()
            )

            Spacer (
                modifier = Modifier
                    .padding(1.dp)
            )
        }
    }
}