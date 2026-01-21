package com.example.financeapp.dailytipscreen

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import com.example.financeapp.badges.BadgesViewModel
import com.example.financeapp.ui.theme.LocalAppColors

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.financeapp.database.Badge

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BadgesSection (
    modifier: Modifier = Modifier,
    badgesViewModel: BadgesViewModel
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
                    text = "Delete",
                    color = colors.secondary
                )
            },
            onClick = {
                showMenu = false
            }
        )
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
                    showWallpaper = true
                    temporaryBadge = badge
                }
            )

            Spacer (
                modifier = Modifier
                    .padding(1.dp)
            )
        }
    }
}