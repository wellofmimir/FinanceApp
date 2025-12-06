package com.example.financeapp.goalhistoryscreen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.ui.theme.Pistachio
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.ui.theme.Emerald

@Composable
fun TotalGoalsAchievedSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    var totalGoalsAchievedSectionViewModel: TotalGoalsAchievedSectionViewModel = viewModel (
        factory = object: ViewModelProvider.Factory {
            override fun <T: ViewModel> create(modelClass: Class<T>): T {

                val database = FinanceAppDatabase.Companion.getInstance(context)
                val repository = GoalRepository(database)

                return TotalGoalsAchievedSectionViewModel(repository) as T
            }
        }
    )

    val goals by totalGoalsAchievedSectionViewModel.goals.collectAsState()

    Box (
        modifier = modifier
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column (
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text (
                text = "${goals.size}",
                textAlign = TextAlign.Center,
                fontSize = 128.sp,
                fontWeight = FontWeight.Bold,
                color = Emerald
            )

            Text (
                text = "total goals achieved",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Emerald
            )
        }

    }
}