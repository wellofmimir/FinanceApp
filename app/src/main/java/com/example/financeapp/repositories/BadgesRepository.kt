package com.example.financeapp.repositories

import com.example.financeapp.badges.BadgeCatalog
import com.example.financeapp.badges.BadgeIdentifier
import com.example.financeapp.database.Badge
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.WallpaperClient

import com.example.financeapp.commonutils.FileProvider

class BadgesRepository private constructor (private val database: FinanceAppDatabase, private val fileProvider: FileProvider) {

    companion object {
        private var instance: BadgesRepository? = null

        fun getInstance(database: FinanceAppDatabase, fileProvider: FileProvider): BadgesRepository {
            if (instance == null)
                instance = BadgesRepository(database, fileProvider)

            return instance!!
        }
    }

    val wallpaperClient = WallpaperClient.getInstance()

    suspend fun fetchWallpaperWelcome() {
        val result = wallpaperClient.fetchWallpaperWelcome()

        if (result == null)
            return

        val welcomeWallpaperFile = fileProvider.getWallpaper("welcome.png")

        welcomeWallpaperFile.outputStream().use {
            it.write(result)
        }

        //database.insertBadge(badge)
    }

    suspend fun fetchWallpaperSevenDaysStreak() {
        val result = wallpaperClient.fetchWallpaperSevenDaysStreak()

        if (result == null)
            return

        val sevenDaysStreakWallpaperFile = fileProvider.getWallpaper("sevenDaysStreak.png")

        sevenDaysStreakWallpaperFile.outputStream().use {
            it.write(result)
        }


    }

    suspend fun fetchWallpaperFirstQuote() {
        val result = wallpaperClient.fetchWallpaperFirstQuote()

        if (result == null)
            return

        val wallpaperFirstQuoteFile = fileProvider.getWallpaper("wallpaperFirstQuote.png")

        wallpaperFirstQuoteFile.outputStream().use {
            it.write(result)
        }

        val badge = BadgeCatalog.getBadge(BadgeIdentifier.FIRST_QUOTE_LIKED)
        badge.pathToImage = wallpaperFirstQuoteFile.absolutePath

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
}