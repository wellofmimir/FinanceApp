package com.example.financeapp.network

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient

import okhttp3.Request

interface DailyTipCallback {
    fun result(response: String)
}

data class DailyTip (
    val title: String,
    val tip: String
)

class DailyTipClient private constructor() {
    companion object {
        private var instance: DailyTipClient? = null

        fun getInstance(): DailyTipClient {
            if (instance == null)
                instance = DailyTipClient()

            return instance!!
        }
    }
    private val client = OkHttpClient()
    var hasError = false

    fun fetchDailyTip(callback: DailyTipCallback) {

        val request = Request
            .Builder()
            .get()
            .url("https://shortlyfi.me/api/fact")
            .build()

        Thread {
            try {
                val response = client
                    .newCall(request)
                    .execute()

                val responseBody = response.body?.string()
                response.close()

                Handler (Looper.getMainLooper()).post {
                    callback.result(responseBody!!)
                }

            } catch (e: Exception) {
                hasError = true

                Handler (Looper.getMainLooper()).post {
                    callback.result("")
                }
            }
        }.start()
    }
}