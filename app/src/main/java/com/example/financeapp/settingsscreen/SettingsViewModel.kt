package com.example.financeapp.settingsscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.FeedbackRepository

class SettingsViewModel(private val repository: FeedbackRepository): ViewModel() {

    val isFeedbackAlreadySent = repository.isFeedbackAlreadySent

    fun feedbackAlreadySent(): Boolean {
        val isSent = repository.feedbackAlreadySent()
        return isSent
    }

    fun sendFeedback(name: String, text: String) {
        repository.sendFeedback(name, text)
        feedbackAlreadySent()
    }
}