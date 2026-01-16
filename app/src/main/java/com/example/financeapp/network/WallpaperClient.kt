package com.example.financeapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
class WallpaperClient private constructor() {

    companion object {
        private var instance: WallpaperClient? = null

        fun getInstance(): WallpaperClient {
            if (instance == null)
                instance = WallpaperClient()

            return instance!!
        }
    }

    private val client = OkHttpClient()
    var hasError = false

    suspend fun fetchWallpaperWelcome(): ByteArray? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url(url = "https://shortlyfi.me/api/badges/image/wallpaperWelcome")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.bytes()
            }
        } catch (e: Exception) {
            hasError = true
            null
        }
    }

    suspend fun fetchWallpaperSevenDaysStreak(): ByteArray? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url(url = "https://shortlyfi.me/api/badges/image/sevenDaysStreak")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.bytes()
            }
        } catch (e: Exception) {
            hasError = true
            null
        }
    }
}