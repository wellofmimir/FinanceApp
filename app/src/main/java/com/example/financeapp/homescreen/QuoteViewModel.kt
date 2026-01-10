package com.example.financeapp.homescreen

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import com.example.financeapp.database.Goal

import com.example.financeapp.database.Quote
import com.example.financeapp.repositories.QuoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    private var internQuote = MutableStateFlow<Quote>(Quote(0, "Thinking of a quote...", "The Greeen Team", ""))
    val quote = internQuote.asStateFlow()

    private val internIsLoading = MutableStateFlow(false)
    val isLoading = internIsLoading.asStateFlow()

    private val internHasLoaded = MutableStateFlow(false)

    private val internLikedQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val likedQuotes = internLikedQuotes.asStateFlow()
    private var internQuoteLiked = MutableStateFlow(false)
    val quoteLiked = internQuoteLiked.asStateFlow()

    private fun isQuoteLiked(quote: Quote): Boolean {
        return internLikedQuotes.value.any { likedQuote ->
            quote.quote == likedQuote.quote
        }
    }

    fun loadQuoteWithDelay() {
        viewModelScope.launch {

            if (internHasLoaded.value)
                return@launch

            internIsLoading.value = true

            delay(3000)
            getLikedQuotes()
            fetchQuote()

            internIsLoading.value = false
            internHasLoaded.value = true
        }
    }

    fun toggleQuote(quote: Quote) {
        if (isQuoteLiked(quote)) {
            repository.deleteQuote(quote.quote)
            internQuoteLiked.value = false
        } else {
            repository.insertQuote(quote.quote, quote.name)
            internQuoteLiked.value = true
        }

        getLikedQuotes()
    }

    fun hasError(): Boolean {
        return repository.hasError()
    }

    fun getLikedQuotes() {
        internLikedQuotes.value = repository.getLikedQuotes()
    }

    suspend fun fetchQuote() {

        if (internHasLoaded.value)
            internHasLoaded.value = false

        repository.fetchQuoteFromServer()

        internQuote.value = internQuote.value.copy (
            id = repository.getDailyQuote().id,
            quote = repository.getDailyQuote().quote,
            name = repository.getDailyQuote().name,
            date = repository.getDailyQuote().date
        )

        internQuoteLiked.value = isQuoteLiked(internQuote.value)
    }
}