package studio.lemniscate.greeen.settingsscreen

import studio.lemniscate.greeen.repositories.CurrencyRepository
import studio.lemniscate.greeen.repositories.FeedbackRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(private val feedbackRepository: FeedbackRepository, private val currencyRepository: CurrencyRepository): ViewModel() {
    private val internFeedbackAlreadySent = MutableStateFlow(false)
    val isFeedbackAlreadySent = internFeedbackAlreadySent.asStateFlow()
    val currency = currencyRepository.currency

    fun isFeedBackSent() {
        internFeedbackAlreadySent.value = feedbackRepository.feedbackAlreadySent()
    }
    fun sendFeedback(name: String, text: String) {
        viewModelScope.launch {
            internFeedbackAlreadySent.value = true
            feedbackRepository.sendFeedback(name, text)
        }
    }

    fun getCurrency() {
        currencyRepository.getCurrency()
    }

    fun setCurrency(currency: String) {
        currencyRepository.setCurrency(currency)
    }
}