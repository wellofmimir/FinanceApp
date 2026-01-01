package com.example.financeapp.likedquotes

import com.example.financeapp.database.Quote
import com.example.financeapp.repositories.QuoteRepository

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class LikedQuotesSectionViewModel(private val repository: QuoteRepository) : ViewModel() {

    private val internLikedQuotes = MutableStateFlow<List<Quote>>(emptyList())
    val likedQuotes = internLikedQuotes.asStateFlow()

    fun getLikedQuotes() {
        internLikedQuotes.value = repository.getLikedQuotes()
    }
}