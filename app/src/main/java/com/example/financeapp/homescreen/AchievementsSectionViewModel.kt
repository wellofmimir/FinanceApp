package com.example.financeapp.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AchievementsSectionViewModel(private val repository: GoalRepository): ViewModel() {

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()

    private fun insertExampleGoals() {

        val exampleGoals = listOf (
            Goal(1, "my awesome goal", 1100.0f, 0f, 2, "November 14, 2025", 4),
            Goal(1, "example goal #2", 1100.0f, 0f, 2, "November 01, 2025", 3),
            Goal(1, "pay the Loch Ness Monster", 1101.0f, 0f, 2, "Oktober 25, 2025", 1),
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