package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WelcomeScreenViewModel(private val userRepository: UserRepository, private val goalRepository: GoalRepository): ViewModel() {

    fun insertGoal(goal: Goal) {

        val currentDate = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        var newGoal = Goal(goal.id, goal.goal, goal.amount, goal.saved, goal.idStatus, formattedDate, goal.tokenCount)
        goalRepository.insertGoal(newGoal)

        val newestGoalId = goalRepository.getNewestGoalId()
        newGoal = Goal(newestGoalId, goal.goal, goal.amount, goal.saved, goal.idStatus, formattedDate, goal.tokenCount)
        goalRepository.setCurrentGoal(newGoal)
    }

    fun updateUser(user: String) {
        userRepository.updateUser(user)
    }
}