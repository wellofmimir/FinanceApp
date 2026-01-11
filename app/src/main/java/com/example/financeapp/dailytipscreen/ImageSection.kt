package com.example.financeapp.dailytipscreen
import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Alignment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState


@Composable
fun ImageSection(modifier: Modifier = Modifier, dailyTipScreenViewModel: DailyTipScreenViewModel) {

    val colors = LocalAppColors.current
    var showDialog by remember { mutableStateOf(false) }
    val bitmap by dailyTipScreenViewModel.imageToDailyTip.collectAsState()

    if (showDialog) {
        Dialog (
            onDismissRequest = {
                showDialog = false
            }
        ) {
            bitmap?.let {
                Image (
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            showDialog = false
                        }
                )
            }
        }
    }

    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .border (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp),
                width = 1.dp
            )
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .clickable (
            ) {
                showDialog = true
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            contentAlignment = Alignment.Center
        ) {
            bitmap?.let {
                Image (
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}