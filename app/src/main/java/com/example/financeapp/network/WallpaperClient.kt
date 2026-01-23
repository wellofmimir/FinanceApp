package com.example.financeapp.network

import androidx.compose.material3.Badge
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
            BadgeIdentifier.SEVEN_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SEVEN_QUOTES_LIKED).pathToImage
            BadgeIdentifier.FOURTEEN_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.FOURTEEN_QUOTES_LIKED).pathToImage
            BadgeIdentifier.FORTY_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.FORTY_QUOTES_LIKED).pathToImage
            BadgeIdentifier.SEVENTY_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SEVENTY_QUOTES_LIKED).pathToImage
            BadgeIdentifier.HUNDRED_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.HUNDRED_QUOTES_LIKED).pathToImage
            BadgeIdentifier.ONE_THIRTY_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.ONE_THIRTY_QUOTES_LIKED).pathToImage
            BadgeIdentifier.ONE_NINETY_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.ONE_NINETY_QUOTES_LIKED).pathToImage
            BadgeIdentifier.THREE_SIXTY_QUOTES_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.THREE_SIXTY_QUOTES_LIKED).pathToImage

            BadgeIdentifier.FIRST_RECEIPT -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_RECEIPT).pathToImage
            BadgeIdentifier.TEN_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.TEN_RECEIPTS).pathToImage
            BadgeIdentifier.THIRTY_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_RECEIPTS).pathToImage
            BadgeIdentifier.FIFTY_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.FIFTY_RECEIPTS).pathToImage
            BadgeIdentifier.HUNDRED_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.HUNDRED_RECEIPTS).pathToImage
            BadgeIdentifier.FIVE_HUNDRED_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.FIVE_HUNDRED_RECEIPTS).pathToImage
            BadgeIdentifier.THOUSAND_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.THOUSAND_RECEIPTS).pathToImage

            BadgeIdentifier.FIRST_GOAL -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_GOAL).pathToImage
            BadgeIdentifier.SECOND_GOAL -> BadgeCatalog.getBadge(BadgeIdentifier.SECOND_GOAL).pathToImage
            BadgeIdentifier.FIVE_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.FIVE_GOALS).pathToImage
            BadgeIdentifier.TEN_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.TEN_GOALS).pathToImage
            BadgeIdentifier.THIRTY_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_GOALS).pathToImage
            BadgeIdentifier.FIFTY_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.FIFTY_GOALS).pathToImage

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