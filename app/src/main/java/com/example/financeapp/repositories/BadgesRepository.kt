package com.example.financeapp.repositories

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

        val badge = Badge (
            title = "Welcome",
            text = "Welcome to Green!",
            theme = "",
            pathToImage = welcomeWallpaperFile.absolutePath
        )

        database.insertBadge(badge)
    }

    suspend fun fetchWallpaperSevenDaysStreak() {
        val result = wallpaperClient.fetchWallpaperSevenDaysStreak()

        if (result == null)
            return

        val sevenDaysStreakWallpaperFile = fileProvider.getWallpaper("sevenDaysStreak.png")

        sevenDaysStreakWallpaperFile.outputStream().use {
            it.write(result)
        }

        val badge = Badge (
            title = "Seven days at it!",
            text = "You start to build a habit. Keep it going!",
            theme = "",
            pathToImage = sevenDaysStreakWallpaperFile.absolutePath
        )

        database.insertBadge(badge)
    }

    fun insertUserBadge(badge: Badge) {
        database.insertBadge(badge)
    }

    fun loadUserBadges(): List<Badge> {
        return database.loadUserBadges()
    }
}