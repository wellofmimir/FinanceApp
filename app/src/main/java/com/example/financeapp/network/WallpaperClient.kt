package com.example.financeapp.network

import com.example.financeapp.badges.BadgeCatalog
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

    suspend fun fetchWallpaper (
        badgeIdentifier: BadgeIdentifier
    ): ByteArray? = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val endpoint = when (badgeIdentifier) {
            BadgeIdentifier.FIRST_QUOTE_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_QUOTE_LIKED).pathToImage
            BadgeIdentifier.FIRST_RECEIPT -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_RECEIPT).pathToImage
            BadgeIdentifier.FIRST_GOAL -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_GOAL).pathToImage
            BadgeIdentifier.FIRST_DAILY_TIP_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_DAILY_TIP_LIKED).pathToImage
            BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.NINETY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.NINETY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.ONE_EIGHTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.ONE_EIGHTY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.THREE_SIXTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.THREE_SIXTY_DAILY_TIPS_LIKED).pathToImage
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