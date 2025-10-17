package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecentlyCompletedGoalsSectionViewModel(private val repository: GoalRepository) : ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    private fun insertExampleGoals() {

        val exampleGoals = listOf (
            Goal(1, "my awesome goal", 1101.0f, 2),
            Goal(1, "example goal #2", 1101.0f, 2),
            Goal(1, "pay the Loch Ness Monster", 1101.0f, 2)
        )

        internGoals.value = exampleGoals
    }

    fun getCompletedGoals() {

        viewModelScope.launch {
            val result = repository.getCompletedGoals()
            internGoals.value = result

            if (internGoals.value.isEmpty())
                insertExampleGoals()
        }
    }
}