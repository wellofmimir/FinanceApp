package com.example.financeapp

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class QuoteRepository private constructor(val database: FinanceAppDatabase) {

    companion object {
        private var instance: QuoteRepository? = null

        fun getInstance(database: FinanceAppDatabase): QuoteRepository {
            if (instance == null)
                instance = QuoteRepository(database)

            return instance!!
        }
    }

    private var internDailyQuoteObtained = MutableStateFlow(false)

    private var internQuoteToQuotedPerson = MutableStateFlow<Pair<String, String>>("" to "")
    var quoteToQuotedPerson = internQuoteToQuotedPerson.asStateFlow()
    private val client = QuoteClient.getInstance()

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

    fun updateCurrentQuote(currentQuote: String) {
        database.updateCurrentQuote(currentQuote)
    }

    fun insertQuote(quote: String, name: String) {

        val currentDate = java.time.LocalDate.now()
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMMM d, yyyy", java.util.Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        database.insertQuote(quote, name, formattedDate)
    }

    fun fetchQuoteFromServer() {

        //Ich will den Server nicht hardcore penetrieren,
        //da jede Abfrage mich bares Geld kostet.
        //Deswegen wird der Server nur einmal täglich abgefragt,
        //und das Ergebnis dann in den Shared Preferences gespeichert und davon bei Abfrage zurückgegeben.

        if (database.dailyQuoteFetched()) {
            internQuoteToQuotedPerson.value = database.dailyQuote()
            return
        }

        val result = client.fetchQuote(object: QuoteClientCallback {
            override fun result(response: String) {

                if (isValidJson(response) == false) {

                    internQuoteToQuotedPerson.value = Pair<String, String>("A little error occurred - no quote today :(", "Anonymous")
                    return;
                }

                if (!response.startsWith("{")) {

                    internQuoteToQuotedPerson.value = Pair<String, String>("A little error occurred - no quote today :(", "Anonymous")
                    return;
                }

                val jsonObject = JSONObject(response);
                internQuoteToQuotedPerson.value = Pair<String, String>(jsonObject.getString("quote"), jsonObject.getString("name"))
                database.saveDailyQuoteFetched(internQuoteToQuotedPerson.value)
            }
        })
    }

    fun getCurrentQuote(): String {
        return database.getCurrentQuote()
    }

    fun deleteQuote(quote: String) {
        database.deleteQuote(quote)
    }
}