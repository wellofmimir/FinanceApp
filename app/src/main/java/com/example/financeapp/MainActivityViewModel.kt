package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: UserRepository): ViewModel() {
    private var internUser = MutableStateFlow<String>("")
    var user = internUser.asStateFlow()

    var isTutorialDone = repository.isTutorialDone()

    fun loadUser() {
        internUser.value = repository.getUser()
    }

    fun updateTutorialDoneStatus(status: Boolean) {
        repository.updateTutorialDoneStatus(status)
    }
}