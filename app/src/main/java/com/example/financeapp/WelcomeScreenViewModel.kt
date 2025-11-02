package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class WelcomeScreenViewModel(private val repository: UserRepository): ViewModel() {

    fun updateUser(user: String) {
        repository.updateUser(user)
    }
}