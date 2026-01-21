package com.example.financeapp.commonutils

import com.example.financeapp.badges.BadgesViewModel
import com.example.financeapp.homescreen.GoalsSectionViewModel
import com.example.financeapp.receiptsscreen.ReceiptSectionsViewModel

import android.widget.Toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.financeapp.homescreen.QuoteViewModel

@Composable
fun GlobalToastHandler (
    badgesViewModel: BadgesViewModel,
    goalsSectionViewModel: GoalsSectionViewModel,
    receiptSectionsViewModel: ReceiptSectionsViewModel,
    quoteViewModel: QuoteViewModel
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        badgesViewModel.toastEvent.collect { message ->
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        goalsSectionViewModel.toastEvent.collect { message ->
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        receiptSectionsViewModel.toastEvent.collect { message ->
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        quoteViewModel.toastEvent.collect { message ->
            Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }
}