package com.example.financeapp.network

import com.example.financeapp.badges.BadgeIdentifier
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

    suspend fun fetchWallpaper(badgeIdentifier: BadgeIdentifier): ByteArray? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val endpoint = when (badgeIdentifier) {
            BadgeIdentifier.FIRST_QUOTE_LIKED -> "wallpaperFirstQuote.png"
            BadgeIdentifier.FIRST_RECEIPT -> "wallpaperFirstReceipt.png"
        }

        val request = Request.Builder()
            .get()
            .url(url = "https://shortlyfi.me/api/badges/image/$endpoint")
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