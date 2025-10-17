package com.example.financeapp

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AddNewGoalMenuViewModel(private val repository: GoalRepository) : ViewModel() {

    fun newGoalAdded(goal: String, amount: Float, statusDescription: String) {

        repository.insertGoal(goal, amount, statusDescription)
    }
}