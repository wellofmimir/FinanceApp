package com.example.financeapp.goalhistoryscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.GoalRepository
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