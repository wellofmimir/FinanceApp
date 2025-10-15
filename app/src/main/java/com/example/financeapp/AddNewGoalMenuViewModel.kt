package com.example.financeapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddNewGoalMenuViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    fun newGoalAdded(goal: String, amount: Float) {

        database.insertGoal(goal, amount)
    }
}