package com.example.financeapp.dailytipscreen
import com.example.financeapp.repositories.DailyTipRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.network.DailyTip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DailyTipScreenViewModel(private val repository: DailyTipRepository): ViewModel() {

    private var internDailyTip = MutableStateFlow<DailyTip>(DailyTip("", ""))
    var dailyTip = internDailyTip.asStateFlow()

    fun newDailyTipAvailable(): Boolean {
        return repository.newDailyTipAvailable()
    }

    fun interstitialAdAfterDailyTipSeen(): Boolean {
        return repository.interstitialAdAfterDailyTipSeen()
    }

    fun setInterstitialAdAfterDailyTipSeen() {
        repository.setInterstitialAdAfterDailyTipSeen()
    }

    fun fetchDailyTip() {
        viewModelScope.launch {
            repository.fetchDailyTipFromServer()
            internDailyTip.value = repository.getDailyTip()
        }
    }
}