package com.example.financeapp.repositories

import com.example.financeapp.network.QuoteClient
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.database.Quote
import com.example.financeapp.commonutils.isValidJson

import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class QuoteRepository private constructor (val database: FinanceAppDatabase) {

    companion object {
        private var instance: QuoteRepository? = null

        fun getInstance(database: FinanceAppDatabase): QuoteRepository {
            if (instance == null)
                instance = QuoteRepository(database)

            return instance!!
        }
    }


    private val client = QuoteClient.getInstance()

    fun hasError(): Boolean {
        return client.hasError
    }

    fun getLikedQuotes(): List<Quote> {
        return database.getAllQuotes()
    }

    fun getDailyQuote(): Quote {
        val dailyQuote = database.dailyQuote()
        return Quote(1, dailyQuote.first, dailyQuote.second, "")
    }

    fun insertQuote(quote: String, name: String) {

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        database.insertQuote(quote, name, formattedDate)
    }

    suspend fun fetchQuoteFromServer(): Quote {

        //Ich will den Server nicht hardcore penetrieren,
        //da jede Abfrage mich bares Geld kostet.
        //Deswegen wird der Server nur einmal täglich abgefragt,
        //und das Ergebnis dann in den Shared Preferences gespeichert und davon bei Abfrage zurückgegeben.

        if (database.dailyQuoteFetched()) {
            return getDailyQuote()
        }

        val result = client.fetchQuote()

        if (isValidJson(result) == false) {
            val dailyQuote = Pair("A little error occurred - no quote today :(", "Anonymous")
            return Quote(1, dailyQuote.first, dailyQuote.second, "")
        }

        if (!result.startsWith("{")) {
            val dailyQuote = Pair("A little error occurred - no quote today :(", "Anonymous")
            return Quote(1, dailyQuote.first, dailyQuote.second, "")
        }

        val jsonObject = JSONObject(result)
        val dailyQuote = Pair<String, String>(jsonObject.getString("quote"), jsonObject.getString("person"))
        database.setDailyQuote(dailyQuote)

        return Quote(1, dailyQuote.first, dailyQuote.second, "")
    }

    fun deleteQuote(quote: String) {
        database.deleteQuote(quote)
    }
}