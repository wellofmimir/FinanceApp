package com.example.financeapp

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

interface QuoteClientCallback {
    fun onResult(response: String?)
}

class QuoteClient {
    private val client = OkHttpClient()
    fun getQuote(callback: QuoteClientCallback) {

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
                    callback.onResult(responseBody)
                }

            } catch (e: Exception) {

                Handler(Looper.getMainLooper()).post {
                    callback.onResult(null)
                }
            }
        }.start()
    }
}