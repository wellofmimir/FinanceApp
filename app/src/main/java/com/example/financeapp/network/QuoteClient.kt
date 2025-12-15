package com.example.financeapp.network

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request

interface QuoteClientCallback {
    fun result(response: String)
}
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
    fun fetchQuote(callback: QuoteClientCallback) {

        val request = Request.Builder()
            .get()
            .url("https://shortlyfi.me/api/quote")
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                response.close()

                Handler (Looper.getMainLooper()).post {
                    callback.result(responseBody!!)
                }
            } catch (e: kotlin.Exception) {
                hasError = true

                Handler (Looper.getMainLooper()).post {
                    callback.result("")
                }
            }
        }.start()
    }
}