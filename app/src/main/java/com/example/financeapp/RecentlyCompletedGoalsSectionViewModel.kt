package com.example.financeapp

import androidx.lifecycle.ViewModel

class RecentlyCompletedGoalsSectionViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    fun getIDGoalStatus(statusDescription: String): GoalStatus {
        return database.getIDGoalStatus(statusDescription)!!
    }
}