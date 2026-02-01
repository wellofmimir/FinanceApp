package com.example.financeapp.metricsscreen

import com.example.financeapp.advertisement.RewardedAdManager
import com.example.financeapp.repositories.MetricsRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.network.TrendRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MetricsScreenViewModel (
    private val rewardedAdManager: RewardedAdManager,
    private val repository: MetricsRepository
): ViewModel() {

    private var internDailyTrend = MutableStateFlow("")
    val dailyTrend = internDailyTrend.asStateFlow()

    private var waitingTexts = listOf (
        "Hang tight! We’re looking at your spending patterns.",
        "Your personal trend report is on the way…",
        "Spotting trends in your spending…",
        "Your trend is brewing… like a fine cup of coffee!",
        "Processing your trend insights… please wait.",
        "Checking your expenses and calculating trends…",
        "Your personal spending trends are almost ready…",
        "One moment! We’re checking your numbers carefully.",
        "Scanning your daily spendings…",
        "Looking for spikes, dips, and unusual patterns…",
        "Analyzing…",
        "Almost ready…"
    )

    private var internWaitingText = MutableStateFlow("")
    val waitingText = internWaitingText.asStateFlow()

    private var internWaitingForDailyTrend = MutableStateFlow(false)
    val waitingForDailyTrend = internWaitingForDailyTrend.asStateFlow()

    fun getDailyTrend(trendRequest: TrendRequest) {
        viewModelScope.launch {
            internWaitingText.value = waitingTexts.random()
            internWaitingForDailyTrend.value = true
            val result = repository.getDailyTrend(trendRequest)
            internDailyTrend.value = result
            internWaitingForDailyTrend.value = false
        }
    }
}