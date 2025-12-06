package com.example.financeapp.homescreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.GoalRepository

class AddNewGoalMenuViewModel(private val repository: GoalRepository) : ViewModel() {

    fun newGoalAdded(goal: String, amount: Float, statusDescription: String) {
        repository.insertGoal(goal, amount, 0f, statusDescription)
    }
}