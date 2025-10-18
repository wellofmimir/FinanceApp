package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoalprogressSectionViewModel(private val repository: GoalRepository) : ViewModel() {
    private val internCurrentGoal = MutableStateFlow<Goal?>(null)
    val currentGoal = internCurrentGoal.asStateFlow()

    fun getCurrentGoal() {

        viewModelScope.launch {
            val result = repository.getCurrentGoal()
            internCurrentGoal.value = result
        }
    }
}