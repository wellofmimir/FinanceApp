package com.example.financeapp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue


class MainActivityViewModel(private val repository: UserRepository): ViewModel() {
    private var internUser = MutableStateFlow<String>("")
    var user = internUser.asStateFlow()

    fun setHomeScreenTutorialDone() {
        repository.setHomeScreenTutorialDone()
    }

    fun getHomeScreenTutorialDone(): Boolean {
        return repository.getHomeScreenTutorialDone()
    }

    fun resetHomeScreenTutorialDone() {
        repository.resetHomeScreenTutorialDone()
    }

    fun loadUser() {
        internUser.value = repository.getUser()
    }

    fun getReceiptsTutorialDone(): Boolean {
        return repository.getReceiptsTutorialDone()
    }

    fun setReceiptsTutorialDone() {
        repository.setReceiptsTutorialDone()
    }

    fun resetReceiptsTutorialDone() {
        repository.resetReceiptsTutorialDone()
    }

    fun getGoalHistoryTutorialDone(): Boolean {
        return repository.getGoalHistoryTutorialDone()
    }

    fun setGoalHistoryTutorialDone() {
        repository.setGoalHistoryTutorialDone()
    }

    fun resetGoalHistoryTutorialDone() {
        repository.resetGoalHistoryTutorialDone()
    }
}