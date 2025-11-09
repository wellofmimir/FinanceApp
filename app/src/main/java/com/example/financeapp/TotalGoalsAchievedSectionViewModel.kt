package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TotalGoalsAchievedSectionViewModel(private val repository: GoalRepository): ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    fun getCompletedGoals() {

        viewModelScope.launch {

            val result = repository.getCompletedGoals()
            internGoals.value = result
        }
    }
}