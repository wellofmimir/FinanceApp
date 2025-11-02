package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: UserRepository): ViewModel() {
    private var internUser = MutableStateFlow<String>("")
    var user = internUser.asStateFlow()

    fun loadUser() {
        viewModelScope.launch {
            internUser.value = repository.getUser()
        }
    }
}