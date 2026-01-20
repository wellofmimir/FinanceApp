package com.example.financeapp.aboutscreen

import com.example.financeapp.ui.theme.LocalAppColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AboutScreen (
    modifier: Modifier = Modifier
) {
    val colors = LocalAppColors.current
    var showDialog by remember { mutableStateOf(false) }
    val dialogInteractionSource = remember { MutableInteractionSource() }

    if (showDialog)
        Dialog (
            onDismissRequest = {
                showDialog = false
            },
            DialogProperties (
                usePlatformDefaultWidth = false
            )
        ) {
            PrivacyPolicy (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .clickable (
                        indication = null,
                        interactionSource = dialogInteractionSource
                    ) {
                        showDialog = false
                    }
            )
        }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background (
                color = colors.primary
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppLogoSection (
            modifier = modifier
                .weight(0.3f)
        )

        MissionSection (
            modifier = modifier
                .weight(0.7f)
        )

        Text (
            text = "Privacy Policy",
            modifier = modifier
                .fillMaxWidth()
                .weight(0.05f)
                .clickable() {
                    showDialog = true
                },
            textAlign = TextAlign.Center,
            color = colors.secondary
        )
    }


}

