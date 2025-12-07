package com.example.financeapp.homescreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoalprogressSectionViewModel(private val repository: GoalRepository) : ViewModel() {
    private val internCurrentGoal = MutableStateFlow<Goal?>(null)
    val currentGoal = internCurrentGoal.asStateFlow()
    private val internPercentageOfCurrentGoal = MutableStateFlow<Int>(0)
    val percentageOfCurrentGoal = internPercentageOfCurrentGoal.asStateFlow()

    fun getCurrentGoalPercentage(): Int {
        return internPercentageOfCurrentGoal.value
    }

    fun calculateCurrentGoalPercentage() {

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

            when (internPercentageOfCurrentGoal.value) {
                100 -> {
                    setGoalCompleted(goal)
                }
            }
        }
    }


    fun getCurrentGoal() {
        val result = repository.getCurrentGoal()

        if (result == null) {
            internCurrentGoal.value = null
            return
        }

        internCurrentGoal.value = result
    }

    fun updateGoal(goal: Goal) {
        repository.updateGoal(goal)
    }

    fun setGoalCompleted(goal: Goal) {
        repository.setGoalCompleted(goal)
    }

    fun addToTotalTokensEarned(amount: Int) {
        repository.addToTotalTokensEarned(amount)
    }
}