package com.example.financeapp.homescreen

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.database.Goal
import com.example.financeapp.repositories.GoalRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ShareAchievementEvent {
    data class SharedAchievement (val imageUri: Uri, val text: String): ShareAchievementEvent
}
class AchievementsSectionViewModel(private val repository: GoalRepository): ViewModel() {

    private val internShareEventForFacebook = MutableSharedFlow<ShareAchievementEvent>()
    val shareEventForFacebook = internShareEventForFacebook.asSharedFlow()

    fun shareAchievementOnFacebook(goal: Goal) {
        viewModelScope.launch {
            internShareEventForFacebook.emit (
                ShareAchievementEvent.SharedAchievement (
                    imageUri = goal.pathToImage.toUri(),
                    text = "I achieved my goal with the Greeen-App!"
                )
            )
        }
    }
    private val internShareEventForWhatsApp = MutableSharedFlow<ShareAchievementEvent>()
    val shareEventForWhatsApp = internShareEventForWhatsApp.asSharedFlow()

    fun shareAchievementOnWhatsApp(goal: Goal) {
        viewModelScope.launch {
            internShareEventForWhatsApp.emit (
                ShareAchievementEvent.SharedAchievement (
                    imageUri = goal.pathToImage.toUri(),
                    text = "I achieved my goal with the Greeen-App!"
                )
            )
        }
    }

    private val internShareEventForFacebookMessenger = MutableSharedFlow<ShareAchievementEvent>()
    val shareEventForFacebookMessenger = internShareEventForFacebookMessenger.asSharedFlow()

    fun shareAchievementOnFacebookMessenger(goal: Goal) {
        viewModelScope.launch {
            internShareEventForFacebookMessenger.emit (
                ShareAchievementEvent.SharedAchievement (
                    imageUri = goal.pathToImage.toUri(),
                    text = "I achieved my goal with the Greeen-App!"
                )
            )
        }
    }

    private val internGoals = MutableStateFlow<List<Goal>>(emptyList())
    val goals = internGoals.asStateFlow()
    private fun insertExampleGoals() {
        val exampleGoals = listOf (
            Goal(1, "my awesome goal", 1100.0f, 0f, 2, "November 14, 2025", 4, ""),
            Goal(1, "example goal #2", 1100.0f, 0f, 2, "November 01, 2025", 3, ""),
            Goal(1, "pay the Loch Ness Monster", 1101.0f, 0f, 2, "Oktober 25, 2025", 1, ""),
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

    private val internGoalsOrderedRandomly = MutableStateFlow<List<Goal>>(emptyList())
    val goalsOrderedRandomly = internGoalsOrderedRandomly.asStateFlow()

    fun getCompletedGoalsOrderedRandomly() {
        val result = repository.getCompletedGoalsOrderedRandomly()
        internGoalsOrderedRandomly.value = result
    }
}