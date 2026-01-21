package com.example.financeapp.badges

import com.example.financeapp.database.Badge

enum class BadgeIdentifier() {
    FIRST_QUOTE_LIKED,
    FIRST_RECEIPT
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

            BadgeIdentifier.FIRST_RECEIPT -> Badge (
                identifier = BadgeIdentifier.FIRST_RECEIPT.ordinal,
                title = "One receipt at a time!",
                text = "The first step to responsible financials.",
                theme = "",
                pathToImage = "wallpaperFirstReceipt.png",
                isGranted = false
            )
        }
    }
}