package com.example.financeapp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Exception
import org.json.JSONObject
import org.json.JSONArray

class QuoteViewModel(private val repository: QuoteRepository) : ViewModel() {
    val quote = repository.quote
    val quotedPerson = repository.quotedPerson
    var quoteLiked by mutableStateOf(false)
        private set

    fun loadQuote() {
        repository.loadQuote()
    }

    fun quoteGotLiked(quote: String, name: String) {
        quoteLiked = !quoteLiked

        if (quoteLiked) {
            repository.insertQuote(quote, name)
        } else {
            repository.deleteQuote(quote)
        }
    }
}
