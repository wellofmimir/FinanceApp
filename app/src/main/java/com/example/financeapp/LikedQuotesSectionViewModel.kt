package com.example.financeapp

import androidx.lifecycle.ViewModel

class LikedQuotesSectionViewModel(private val repository: QuoteRepository) : ViewModel() {

    fun getLikedQuotes(): List<Quote> {
        return repository.getLikedQuotes()
    }
}