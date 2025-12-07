package com.example.financeapp.homescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

import com.example.financeapp.database.Quote
import com.example.financeapp.repositories.QuoteRepository

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {

    val quoteToQuotedPerson = repository.quoteToQuotedPerson

    var quoteLiked by mutableStateOf(false)
        private set

    fun quoteGotLiked(quote: String, name: String) {
        quoteLiked = !quoteLiked

        if (quoteLiked) {
            repository.insertQuote(quote, name)
        } else {
            repository.deleteQuote(quote)
        }
    }

    fun getLikedQuotes(): List<Quote> {
        return repository.getLikedQuotes()
    }

    fun fetchQuote() {
        repository.fetchQuoteFromServer()
    }
}