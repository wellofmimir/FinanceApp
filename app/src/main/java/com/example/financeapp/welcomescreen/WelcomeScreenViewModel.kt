package com.example.financeapp.welcomescreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import com.example.financeapp.repositories.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class WelcomeScreenViewModel(private val userRepository: UserRepository, private val goalRepository: GoalRepository): ViewModel() {

    fun insertGoal(goal: Goal) {

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        var newGoal = Goal(
            goal.id,
            goal.goal,
            goal.amount,
            goal.saved,
            goal.idStatus,
            formattedDate,
            goal.tokenCount
        )
        goalRepository.insertGoal(newGoal)

        val newestGoalId = goalRepository.getNewestGoalId()
        newGoal = Goal(
            newestGoalId,
            goal.goal,
            goal.amount,
            goal.saved,
            goal.idStatus,
            formattedDate,
            goal.tokenCount
        )
        goalRepository.setCurrentGoal(newGoal)
    }

    fun updateUser(user: String) {
        userRepository.updateUser(user)
    }
}