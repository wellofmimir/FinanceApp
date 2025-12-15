package com.example.financeapp.settingsscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.CurrencyRepository
import com.example.financeapp.repositories.FeedbackRepository


class SettingsViewModel(private val feedbackRepository: FeedbackRepository, private val currencyRepository: CurrencyRepository): ViewModel() {

    val isFeedbackAlreadySent = feedbackRepository.isFeedbackAlreadySent
    val currency = currencyRepository.currency

    fun feedbackAlreadySent(): Boolean {
        val isSent = feedbackRepository.feedbackAlreadySent()
        return isSent
    }
    fun sendFeedback(name: String, text: String) {
        feedbackRepository.sendFeedback(name, text)
        feedbackAlreadySent()
    }

    fun getCurrency() {
        currencyRepository.getCurrency()
    }

    fun setCurrency(currency: String) {
        currencyRepository.setCurrency(currency)
    }
}