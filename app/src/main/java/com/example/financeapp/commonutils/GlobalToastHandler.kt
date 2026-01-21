package com.example.financeapp.commonutils

import com.example.financeapp.badges.BadgesViewModel

import android.widget.Toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun GlobalToastHandler (
    badgesViewModel: BadgesViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        badgesViewModel.toastEvent.collect { message ->
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}