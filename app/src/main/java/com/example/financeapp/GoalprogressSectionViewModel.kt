package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalprogressSectionViewModel(private val repository: GoalRepository) : ViewModel() {
    private val internCurrentGoal = MutableStateFlow<Goal?>(null)
    val currentGoal = internCurrentGoal.asStateFlow()
    private val internPercentageOfCurrentGoal = MutableStateFlow<Int>(0)
    val percentageOfCurrentGoal = internPercentageOfCurrentGoal.asStateFlow()

    fun calculateCurrentGoalPercentage() {

        viewModelScope.launch {

            val goal = internCurrentGoal.value

            if (goal != null && goal.amount != 0.0f) {

                val zaehler = goal.saved
                val nenner = goal.amount
                val result = ((zaehler / nenner) * 100).toInt()

                internPercentageOfCurrentGoal.value = when {
                    result >= 100 -> 100
                    result <= 0 -> 0
                    else -> result
                }
            }

        }
    }
    fun getCurrentGoal() {
        viewModelScope.launch {
            val result = repository.getCurrentGoal()
            internCurrentGoal.value = result
        }
    }

    fun updateGoal(goal: Goal) {
        viewModelScope.launch {
            repository.updateGoal(goal)
        }
    }
}