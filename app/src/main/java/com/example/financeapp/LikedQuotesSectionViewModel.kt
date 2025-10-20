package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LikedQuotesSectionViewModel(private val repository: QuoteRepository) : ViewModel() {

    fun getLikedQuotes(): List<Quote> {
        return repository.getLikedQuotes()
    }
}