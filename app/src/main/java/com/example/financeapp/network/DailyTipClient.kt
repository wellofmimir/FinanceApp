package com.example.financeapp.network

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient

import okhttp3.Request

interface DailyTipCallback {
    fun result(response: String)
}

data class DailyTip (
    var title: String,
    var tip: String,
    var short: String,
    var category: String
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

    suspend fun fetchDailyTip(): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url("https://shortlyfi.me/api/tip")
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