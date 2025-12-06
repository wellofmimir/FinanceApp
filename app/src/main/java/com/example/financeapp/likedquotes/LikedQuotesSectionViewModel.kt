package com.example.financeapp.likedquotes

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Quote
import com.example.financeapp.repositories.QuoteRepository

class LikedQuotesSectionViewModel(private val repository: QuoteRepository) : ViewModel() {

    fun getLikedQuotes(): List<Quote> {
        return repository.getLikedQuotes()
    }
}