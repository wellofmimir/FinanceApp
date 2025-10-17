package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SwapCurrentGoalMenuViewModel(private val repository: GoalRepository) : ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    private val internCurrentGoal = MutableStateFlow<Goal?>(null)

    val goals = internGoals.asStateFlow()
    val currentGoal = internCurrentGoal.asStateFlow()

    fun getInProgressGoals() {

        viewModelScope.launch {
            val result = repository.getInProgressGoals()
            internGoals.value = result
        }
    }

    fun getCurrentGoal() {

        viewModelScope.launch {
            val result = repository.getCurrentGoal()
            internCurrentGoal.value = result
        }
    }

    fun setCurrentGoal(goal: Goal) {

        viewModelScope.launch {
            repository.setCurrentGoal(goal)
        }
    }
}