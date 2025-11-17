package com.example.financeapp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PunchCardSectionViewModel(private val goalRepository: GoalRepository) : ViewModel() {

    private val internTokenSoFar = MutableStateFlow(0)
    val tokenSoFar = internTokenSoFar.asStateFlow()

    fun getTokenSoFarForPunchcard() {
         internTokenSoFar.value = goalRepository.getTokenSoFarForPunchcard()
    }

    fun resetTokenSoFarForPunchcard() {
        goalRepository.resetTokenSoFarForPunchcard()
    }
}