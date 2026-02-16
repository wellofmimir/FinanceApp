package studio.lemniscate.greeen.badges

import studio.lemniscate.greeen.database.Badge
import studio.lemniscate.greeen.R
enum class BadgeIdentifier() {
    FIRST_QUOTE_LIKED,
    SEVEN_QUOTES_LIKED,
    FOURTEEN_QUOTES_LIKED,
    FORTY_QUOTES_LIKED,
    SEVENTY_QUOTES_LIKED,
    HUNDRED_QUOTES_LIKED,

    FIRST_RECEIPT,
    TEN_RECEIPTS,
    THIRTY_RECEIPTS,
    FIFTY_RECEIPTS,
    HUNDRED_RECEIPTS,
    FIRST_GOAL,
    SECOND_GOAL,
    FIVE_GOALS,
    TEN_GOALS,
    THIRTY_GOALS,
    FIFTY_GOALS,

    FIRST_DAILY_TIP_LIKED,
    SEVEN_DAILY_TIPS_LIKED,
    THIRTY_DAILY_TIPS_LIKED,
    SIXTY_DAILY_TIPS_LIKED,
    NINETY_DAILY_TIPS_LIKED
}

object BadgeCatalog {
    fun getBadge(badgeIdentifier: BadgeIdentifier): Badge {
        return when (badgeIdentifier) {
            BadgeIdentifier.FIRST_QUOTE_LIKED -> Badge (
                identifier = BadgeIdentifier.FIRST_QUOTE_LIKED.ordinal,
                title = "Badge of Motivation",
                text = "More motivation is waiting for you!",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_one
            )

            BadgeIdentifier.SEVEN_QUOTES_LIKED -> Badge (
                identifier = BadgeIdentifier.SEVEN_QUOTES_LIKED.ordinal,
                title = "Quiet Wisdom",
                text = "You connected with seven mindful thoughts. Steady minds make steady choices, especially with money.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_seven
            )

            BadgeIdentifier.FOURTEEN_QUOTES_LIKED -> Badge (
                identifier = BadgeIdentifier.FOURTEEN_QUOTES_LIKED.ordinal,
                title = "Gentle Continuity",
                text = "You kept coming back, without pressure. Calm attention creates progress that feels safe and sustainable.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_fourteen
            )

            BadgeIdentifier.FORTY_QUOTES_LIKED -> Badge (
                identifier = BadgeIdentifier.FORTY_QUOTES_LIKED.ordinal,
                title = "Unstoppable Momentum",
                text = "You didn’t slow down — you leveled up. This kind of consistency builds confidence, discipline, and real progress.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_forty
            )

            BadgeIdentifier.SEVENTY_QUOTES_LIKED -> Badge (
                identifier = BadgeIdentifier.SEVENTY_QUOTES_LIKED.ordinal,
                title = "Deepening Insight",
                text = "Each reflection adds clarity and strengthens your mindful awareness.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_seventy
            )

            BadgeIdentifier.HUNDRED_QUOTES_LIKED -> Badge (
                identifier = BadgeIdentifier.HUNDRED_QUOTES_LIKED.ordinal,
                title = "Thought Collector",
                text = "You’ve marked one hundred ideas worth keeping. Every insight strengthens your understanding and awareness.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.quote_hundred
            )

            BadgeIdentifier.FIRST_RECEIPT -> Badge (
                identifier = BadgeIdentifier.FIRST_RECEIPT.ordinal,
                title = "One Receipt at a Time",
                text = "The first step to responsible financials.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.receipt_one
            )

            BadgeIdentifier.TEN_RECEIPTS -> Badge (
                identifier = BadgeIdentifier.TEN_RECEIPTS.ordinal,
                title = "Getting Started",
                text = "You’ve scanned your first 10 receipts. Every little step counts — keep building the habit.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.receipt_ten
            )

            BadgeIdentifier.THIRTY_RECEIPTS -> Badge (
                identifier = BadgeIdentifier.THIRTY_RECEIPTS.ordinal,
                title = "On Your Way",
                text = "Your routine is taking shape. Small, consistent steps are turning into real progress.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.receipt_thirty
            )

            BadgeIdentifier.FIFTY_RECEIPTS -> Badge (
                identifier = BadgeIdentifier.FIFTY_RECEIPTS.ordinal,
                title = "Momentum Gaining",
                text = "Consistency is paying off. You’re staying organized and making your effort visible.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.receipt_fifty
            )

            BadgeIdentifier.HUNDRED_RECEIPTS -> Badge (
                identifier = BadgeIdentifier.HUNDRED_RECEIPTS.ordinal,
                title = "Habit Locked In",
                text = "Logging receipts has become second nature. You’re mastering the system, step by step.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.receipt_hundred
            )

            BadgeIdentifier.FIRST_GOAL -> Badge (
                identifier = BadgeIdentifier.FIRST_GOAL.ordinal,
                title = "The Journey Starts",
                text = "This is where progress turns into momentum. Keep going, one goal at a time.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_one
            )

            BadgeIdentifier.SECOND_GOAL -> Badge (
                identifier = BadgeIdentifier.SECOND_GOAL.ordinal,
                title = "Gaining Ground",
                text = "Two goals down. Each success builds your confidence and momentum.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_two
            )

            BadgeIdentifier.FIVE_GOALS -> Badge (
                identifier = BadgeIdentifier.FIVE_GOALS.ordinal,
                title = "Finding Your Rhythm",
                text = "You’ve developed a flow. Steady progress is now part of your routine.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_five
            )

            BadgeIdentifier.TEN_GOALS -> Badge (
                identifier = BadgeIdentifier.FIRST_GOAL.ordinal,
                title = "Hitting Your Flow",
                text = "You’re finding your groove and following through. Consistency is becoming natural.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_ten
            )

            BadgeIdentifier.THIRTY_GOALS -> Badge (
                identifier = BadgeIdentifier.FIRST_GOAL.ordinal,
                title = "On Top of Your Game",
                text = "Achieving goals has become a habit. You’re setting the pace and sticking to it.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_thirty
            )

            BadgeIdentifier.FIFTY_GOALS -> Badge (
                identifier = BadgeIdentifier.FIRST_GOAL.ordinal,
                title = "Peak Performance",
                text = "You’ve proven that steady effort leads to real results.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.goal_fifty
            )

            BadgeIdentifier.FIRST_DAILY_TIP_LIKED -> Badge (
                identifier = BadgeIdentifier.FIRST_DAILY_TIP_LIKED.ordinal,
                title = "Investing in Knowledge",
                text = "You liked your first tip — a small action that pays dividends over time. Smart financial journeys start with learning.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.tip_one
            )

            BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.SEVEN_DAILY_TIPS_LIKED.ordinal,
                title = "7 Signals of Growth",
                text = "Seven likes, one direction. Your financial awareness is growing with every valuable insight.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.tip_seven
            )

            BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED.ordinal,
                title = "30 Days Ahead",
                text = "Consistency beats intensity. You’ve built a learning streak that puts you ahead of the curve.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.tip_thirty
            )

            BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.SIXTY_DAILY_TIPS_LIKED.ordinal,
                title = "Consistency Pays",
                text = "60 days of showing up. You’re proving that steady financial learning creates real, lasting progress.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.tip_sixty
            )

            BadgeIdentifier.NINETY_DAILY_TIPS_LIKED -> Badge (
                identifier = BadgeIdentifier.NINETY_DAILY_TIPS_LIKED.ordinal,
                title = "Habit Locked In",
                text = "Three months of daily focus. Financial learning has shifted from effort to routine — and that’s where growth accelerates.",
                theme = "",
                pathToImage = "",
                isGranted = false,
                badgeSymbol = R.drawable.tip_ninety
            )
        }
    }
}