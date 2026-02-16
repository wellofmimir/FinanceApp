package studio.lemniscate.greeen.network

import studio.lemniscate.greeen.badges.BadgeCatalog
import studio.lemniscate.greeen.badges.BadgeIdentifier
import okhttp3.OkHttpClient
import okhttp3.Request
class WallpaperClient (
    private val client: OkHttpClient
) {
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

            BadgeIdentifier.FIRST_RECEIPT -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_RECEIPT).pathToImage
            BadgeIdentifier.TEN_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.TEN_RECEIPTS).pathToImage
            BadgeIdentifier.THIRTY_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_RECEIPTS).pathToImage
            BadgeIdentifier.FIFTY_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.FIFTY_RECEIPTS).pathToImage
            BadgeIdentifier.HUNDRED_RECEIPTS -> BadgeCatalog.getBadge(BadgeIdentifier.HUNDRED_RECEIPTS).pathToImage

            BadgeIdentifier.FIRST_GOAL -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_GOAL).pathToImage
            BadgeIdentifier.SECOND_GOAL -> BadgeCatalog.getBadge(BadgeIdentifier.SECOND_GOAL).pathToImage
            BadgeIdentifier.FIVE_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.FIVE_GOALS).pathToImage
            BadgeIdentifier.TEN_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.TEN_GOALS).pathToImage
            BadgeIdentifier.THIRTY_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_GOALS).pathToImage
            BadgeIdentifier.FIFTY_GOALS -> BadgeCatalog.getBadge(BadgeIdentifier.FIFTY_GOALS).pathToImage

            BadgeIdentifier.FIRST_DAILY_TIP_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.FIRST_DAILY_TIP_LIKED).pathToImage
            BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED).pathToImage
            BadgeIdentifier.NINETY_DAILY_TIPS_LIKED -> BadgeCatalog.getBadge(BadgeIdentifier.NINETY_DAILY_TIPS_LIKED).pathToImage
        }

        val request = Request.Builder()
            .get()
            .url(url = "https://greeen-app.com/api/badges/image/$endpoint")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.bytes()
            }
        } catch (e: Exception) {
            null
        }
    }
}