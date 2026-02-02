package studio.lemniscate.greeen.repositories

import studio.lemniscate.greeen.badges.BadgeCatalog
import studio.lemniscate.greeen.badges.BadgeIdentifier
import studio.lemniscate.greeen.database.Badge
import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.network.WallpaperClient
import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.network.SharedHttpClient

class BadgesRepository private constructor (
    private val database: FinanceAppDatabase,
    private val fileProvider: FileProvider
) {
    companion object {
        private var instance: BadgesRepository? = null

        fun getInstance(database: FinanceAppDatabase, fileProvider: FileProvider): BadgesRepository {
            if (instance == null)
                instance = BadgesRepository(database, fileProvider)

            return instance!!
        }
    }

    val wallpaperClient = WallpaperClient(SharedHttpClient.sharedClient)

    suspend fun fetchWallpaper(badgeIdentifier: BadgeIdentifier) {
        val result = wallpaperClient.fetchWallpaper(badgeIdentifier)

        if (result == null)
            return

        var badge = BadgeCatalog.getBadge(badgeIdentifier)
        val wallpaper = fileProvider.getWallpaper(badge.pathToImage)

        wallpaper.outputStream().use {
            it.write(result)
        }

        badge.pathToImage = wallpaper.absolutePath
        database.insertBadge(badge)
    }

    fun insertUserBadge(badge: Badge) {
        database.insertBadge(badge)
    }

    fun setBadgeGranted(badgeIdentifier: Int, isGranted: Boolean) {
        database.setBadgeGranted(badgeIdentifier, isGranted)
    }

    fun loadUserBadges(): List<Badge> {
        return database.loadBadges()
    }

    fun setBadgeAvailable() {
        database.setBadgeAvailable()
    }

    fun resetBadgeAvailable() {
        database.resetBadgeAvailable()
    }

    fun badgeAvailable(): Boolean {
        return database.badgeAvailable()
    }
}