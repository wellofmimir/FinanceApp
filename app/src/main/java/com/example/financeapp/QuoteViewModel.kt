package com.example.financeapp
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.lang.Exception
import org.json.JSONObject
import org.json.JSONArray

class QuoteViewModel(private val database: FinanceAppDatabase) : ViewModel() {

    private val internQuote = MutableStateFlow("Thinking of a quote...")
    private val internQuotedPerson = MutableStateFlow("Jane Doe")
    val quote = internQuote.asStateFlow()
    val quotedPerson = internQuotedPerson.asStateFlow()
    private val client = QuoteClient()
    var quoteLiked by mutableStateOf(false)
        private set

    private fun isValidJson(json: String): Any {

        return try {
            when {
                json.trim().startsWith("{") -> JSONObject(json)
                json.trim().startsWith("[") -> JSONArray(json)
                else -> false
            }

        } catch (e: Exception) {
            return false
        }
    }

    fun loadQuote() {
        try {
            val result = client.getQuote(object : QuoteClientCallback {
                override fun onResult(response: String?) {

                    if (isValidJson(response!!) == false) {

                        internQuote.value = "A little error occurred - no quote today :("
                        internQuotedPerson.value = "Anonymous"
                        return;
                    }

                    if (!response.startsWith("{")) {

                        internQuote.value = "A little error occurred - no quote today :("
                        internQuotedPerson.value = "Anonymous"
                        return;
                    }

                    val jsonObject = JSONObject(response);

                    internQuote.value = jsonObject.getString("quote")

                    if (!internQuote.value.startsWith("\""))
                        internQuote.value = "\"" + internQuote.value

                    if (!internQuote.value.endsWith("\""))
                        internQuote.value = internQuote.value + "\""

                    internQuotedPerson.value = jsonObject.getString("name")
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
