package com.example.financeapp.network

import okhttp3.OkHttpClient
import okhttp3.Request

data class DailyTip (
    var title: String,
    var tip: String,
    var short: String,
    var category: String,
    var pathToImage: String
)

class DailyTipClient (
    private val client: OkHttpClient
) {

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
            ""
        }
    }

    suspend fun fetchImageToDailyTip(): ByteArray? = kotlinx.coroutines.withContext(context = kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url(url = "https://shortlyfi.me/api/tip/image/newestImage")
            .build()

        try {
            client.newCall(request).execute().use {response ->
                response.body?.bytes()
            }
        } catch (e: Exception) {
            null
        }
    }
}

