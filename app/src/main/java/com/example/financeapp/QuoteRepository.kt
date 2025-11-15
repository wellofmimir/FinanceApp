package com.example.financeapp

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class QuoteRepository private constructor(private val database: FinanceAppDatabase) {

    companion object {
        private var instance: QuoteRepository? = null

        fun getInstance(database: FinanceAppDatabase): QuoteRepository {
            if (instance == null)
                instance = QuoteRepository(database)

            return instance!!
        }
    }
    private val internQuote = MutableStateFlow("")
    private val internQuotedPerson = MutableStateFlow("")
    val quote = internQuote.asStateFlow()
    val quotedPerson = internQuotedPerson.asStateFlow()
    private val client = QuoteClient.getInstance()
    private val allowLoadingAQuote = MutableStateFlow(true)
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

    fun getLikedQuotes(): List<Quote> {
        return database.getAllQuotes()
    }

    fun loadQuote() {

        if (!allowLoadingAQuote.value)
            return;

        try {
            allowLoadingAQuote.value = false

            val result = client.getQuote(object : QuoteClientCallback {
                override fun onResult(response: String?) {

                    if (isValidJson(response!!) == false) {

                        internQuote.value = "A little error occurred - no quote today :("
                        internQuotedPerson.value = "Anonymous"
                        allowLoadingAQuote.value = true
                        return;
                    }

                    if (!response.startsWith("{")) {

                        internQuote.value = "A little error occurred - no quote today :("
                        internQuotedPerson.value = "Anonymous"
                        allowLoadingAQuote.value = true
                        return;
                    }

                    val jsonObject = JSONObject(response);

                    internQuote.value = jsonObject.getString("quote")

                    if (!internQuote.value.startsWith("\""))
                        internQuote.value = "\"" + internQuote.value

                    if (!internQuote.value.endsWith("\""))
                        internQuote.value = internQuote.value + "\""

                    internQuotedPerson.value = jsonObject.getString("name")
                    allowLoadingAQuote.value = false
                }
            })
        } catch (e: Exception) {
            internQuote.value = "No quote today :("
            internQuotedPerson.value = "Johnnie Doe"
            allowLoadingAQuote.value = true
        }
    }

    fun insertQuote(quote: String, name: String) {

        val currentDate = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        database.insertQuote(quote, name, formattedDate)
    }

    fun deleteQuote(quote: String) {
        database.deleteQuote(quote)
    }
}