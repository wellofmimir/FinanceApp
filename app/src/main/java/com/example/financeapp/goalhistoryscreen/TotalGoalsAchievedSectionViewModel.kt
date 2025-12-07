package com.example.financeapp.goalhistoryscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TotalGoalsAchievedSectionViewModel(private val repository: GoalRepository): ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    private val internTotalTokensEarned = MutableStateFlow<Int>(0)
    val totalTokensEarned = internTotalTokensEarned.asStateFlow()

    fun getCompletedGoals() {
        val result = repository.getCompletedGoals()
        internGoals.value = result
    }

    fun getTotalTokensEarned() {
        val result = repository.getTotalTokensEarned()
        internTotalTokensEarned.value = result
    }
}