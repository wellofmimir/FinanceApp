package com.example.financeapp.network

import okhttp3.OkHttpClient
import okhttp3.Request

class QuoteClient private constructor(){
    companion object {
        private var instance: QuoteClient? = null

        fun getInstance(): QuoteClient {
            if (instance == null)
                instance = QuoteClient()

            return instance!!
        }
    }
    private val client = OkHttpClient()
    var hasError: Boolean = false

    suspend fun fetchQuote(): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url("https://shortlyfi.me/api/quote")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.string() ?: ""
            }
        } catch (e: Exception) {
            hasError = true
            ""
        }
    }
}