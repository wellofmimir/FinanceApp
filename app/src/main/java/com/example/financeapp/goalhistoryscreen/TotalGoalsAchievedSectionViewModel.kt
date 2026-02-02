package com.example.financeapp.goalhistoryscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TotalGoalsAchievedSectionViewModel(private val repository: GoalRepository): ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    private val internTotalTokensEarned = MutableStateFlow(0)
    val totalTokensEarned = internTotalTokensEarned.asStateFlow()

    fun getCompletedGoals() {
        viewModelScope.launch {
            val result = repository.getCompletedGoals()
            internGoals.value = result
        }
    }

    fun getTotalTokensEarned() {
        viewModelScope.launch {
            val result = repository.getTotalTokensEarned()
            internTotalTokensEarned.value = result
        }
    }
}