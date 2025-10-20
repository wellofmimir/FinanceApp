package com.example.financeapp
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class HeaderSectionViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    private val internUser = MutableStateFlow("DUMMY")
    val user = internUser.asStateFlow()

    fun getUser() {

        internUser.value = database.getUser()
    }
}