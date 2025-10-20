package com.example.financeapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GoalsSectionViewModel(private val repository: GoalRepository) : ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    fun insertGoal(goal: Goal) {

        viewModelScope.launch {
            repository.insertGoal(goal)
            reloadGoals()
        }
    }

    fun insertExampleGoals() {

        val exampleGoals = listOf (
            Goal(1, "my awesome goal", 1100.0f, 100.50f,1),
            Goal(2, "example goal #2", 12.0f, 4.0f,2),
            Goal(3, "pay the Loch Ness Monster", 3.50f, 0.10f,1),
            Goal(4, "buy a Toyota Corolla", 19999.0f, 15000.80f,2),
            Goal(5, "etf invest", 400.0f, 40.0f,2),
        )

        internGoals.value = exampleGoals
    }

    fun reloadGoals() {

        viewModelScope.launch {

            val result = repository.getInProgressGoals()
            internGoals.value = result
        }

    }
}