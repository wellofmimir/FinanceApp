package com.example.financeapp.header

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.FinanceAppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeaderSectionViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    private val internUser = MutableStateFlow("DUMMY")
    val user = internUser.asStateFlow()

    fun getUser() {
        internUser.value = database.getUser()
    }

    fun updateUser(name: String) {
        database.updateUser(name)
        database.getUser()
    }
}