package com.example.financeapp.badges

import com.example.financeapp.database.Badge

enum class BadgeIdentifier() {
    FIRST_QUOTE_LIKED,
    FIRST_RECEIPT,
    FIRST_GOAL,

    FIRST_DAILY_TIP_LIKED,
    SEVEN_DAILY_TIPS_LIKED,
    THIRTY_DAILY_TIPS_LIKED,
    SIXTY_DAILY_TIPS_LIKED,
    NINETY_DAILY_TIPS_LIKED,
    ONE_EIGHTY_DAILY_TIPS_LIKED,
    THREE_SIXTY_DAILY_TIPS_LIKED
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
                title = "One Receipt at a Time",
                text = "The first step to responsible financials.",
                theme = "",
                pathToImage = "wallpaperFirstReceipt.png",
                isGranted = false
            )

            BadgeIdentifier.FIRST_GOAL -> Badge (
                identifier = BadgeIdentifier.FIRST_GOAL.ordinal,
                title = "The Journey Starts",
                text = "This is where progress turns into momentum. Keep going, one goal at a time.",
                theme = "",
                pathToImage = "wallpaperFirstGoal.png",
                isGranted = false
            )

            BadgeIdentifier.FIRST_DAILY_TIP_LIKED -> Badge (
                identifier = BadgeIdentifier.FIRST_DAILY_TIP_LIKED.ordinal,
                title = "Investing in Knowledge",
                text = "You liked your first tip — a small action that pays dividends over time. Smart financial journeys start with learning.",
                theme = "",
                pathToImage = "wallpaperFirstTip.png",
                isGranted = false
            )

            BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED.ordinal,
                title = "7 Signals of Growth",
                text = "Seven likes, one direction. Your financial awareness is growing with every valuable insight.",
                theme = "",
                pathToImage = "wallpaperSevenTips.png",
                isGranted = false
            )

            BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED.ordinal,
                title = "30 Days Ahead",
                text = "Consistency beats intensity. You’ve built a learning streak that puts you ahead of the curve.",
                theme = "",
                pathToImage = "wallpaperThirtyTips.png",
                isGranted = false
            )

            BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED.ordinal,
                title = "Consistency Pays",
                text = "60 days of showing up. You’re proving that steady financial learning creates real, lasting progress.",
                theme = "",
                pathToImage = "wallpaper60Tips.png",
                isGranted = false
            )

            BadgeIdentifier.NINETY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.NINETY_DAILY_TIPS_LIKED.ordinal,
                title = "Habit Locked In",
                text = "Three months of daily focus. Financial learning has shifted from effort to routine — and that’s where growth accelerates.",
                theme = "",
                pathToImage = "wallpaper90Tips.png",
                isGranted = false
            )

            BadgeIdentifier.ONE_EIGHTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.ONE_EIGHTY_DAILY_TIPS_LIKED.ordinal,
                title = "Built for the Long Game",
                text = "Six months of daily financial focus. You’re no longer chasing results — you’re building them.",
                theme = "",
                pathToImage = "wallpaper180Tips.png",
                isGranted = false
            )

            BadgeIdentifier.THREE_SIXTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.THREE_SIXTY_DAILY_TIPS_LIKED.ordinal,
                title = "Compounding Complete",
                text = "Day after day, insight after insight. You’ve proven that long-term consistency creates exponential results.",
                theme = "",
                pathToImage = "wallpaper360Tips.png",
                isGranted = false
            )
        }
    }
}