package com.example.financeapp
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Exception

class QuoteViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    private val internQuote = MutableStateFlow("Thinking of a quote...")
    private val internQuotedPerson = MutableStateFlow("Jane Doe")
    val quote = internQuote.asStateFlow()
    val quotedPerson = internQuotedPerson.asStateFlow()
    private val client = QuoteClient()
    var quoteLiked by mutableStateOf(false)
        private set

    fun loadQuote() {
        try {
            val result = client.getQuote(object : QuoteClientCallback {
                override fun onResult(response: String?) {
                    internQuote.value = response ?: "No quote today :("
                    internQuotedPerson.value = "John Doe"
                }
            })
        } catch (e: Exception) {
            internQuote.value = "No quote today :("
            internQuotedPerson.value = "Johnnie Doe"
        }
    }

    fun quoteGotLiked(quote: String, name: String) {
        quoteLiked = !quoteLiked

        if (quoteLiked) {
            database.insertQuote(quote, name)
        } else {
            database.deleteQuote(quote)
        }
    }
}
