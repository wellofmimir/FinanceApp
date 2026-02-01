package com.example.financeapp.repositories

import com.example.financeapp.badges.BadgeCatalog
import com.example.financeapp.badges.BadgeIdentifier
import com.example.financeapp.database.Badge
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.WallpaperClient
import com.example.financeapp.commonutils.FileProvider
import com.example.financeapp.network.SharedHttpClient

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