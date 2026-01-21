package com.example.financeapp.badges

import com.example.financeapp.database.Badge

enum class BadgeIdentifier() {
    FIRST_QUOTE_LIKED
}

object BadgeCatalog {

    fun getBadge(badgeIdentifier: BadgeIdentifier): Badge {
        return when (badgeIdentifier) {
            BadgeIdentifier.FIRST_QUOTE_LIKED -> Badge (
                identifier = BadgeIdentifier.FIRST_QUOTE_LIKED.ordinal,
                title = "Badge of Motivation",
                text = "More motivation is waiting for you!",
                theme = "",
                pathToImage = "wallpaperFirstQuote.png",
                isGranted = false
            )
        }
    }
}