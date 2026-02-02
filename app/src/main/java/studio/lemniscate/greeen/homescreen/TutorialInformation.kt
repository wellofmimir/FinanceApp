package studio.lemniscate.greeen.homescreen

import studio.lemniscate.greeen.TutorialStep

data class TutorialInformation (

    var isActive: Boolean,
    var tutorialStep: TutorialStep
)

public fun TutorialInformation.restartHomeScreenTutorial(): TutorialInformation =
    copy (
        isActive = true,
        tutorialStep = TutorialStep.HOMESCREEN_START
    )

public fun TutorialInformation.advanceHomeScreenTutorial(): TutorialInformation =
    copy (
        isActive = true,
        tutorialStep = when (tutorialStep) {
            TutorialStep.HOMESCREEN_START -> TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS
            TutorialStep.HOMESCREEN_RECENTLY_COMPLETED_GOALS -> TutorialStep.HOMESCREEN_CURRENT_GOALS
            TutorialStep.HOMESCREEN_CURRENT_GOALS -> TutorialStep.HOMESCREEN_CURRENT_GOAL
            TutorialStep.HOMESCREEN_CURRENT_GOAL -> TutorialStep.HOMESCREEN_DAILY_FINANCIAL_TIP
            TutorialStep.HOMESCREEN_DAILY_FINANCIAL_TIP -> TutorialStep.HOMESCREEN_QUOTE
            TutorialStep.HOMESCREEN_QUOTE -> TutorialStep.HOMESCREEN_SHOP
            TutorialStep.HOMESCREEN_SHOP -> TutorialStep.HOMESCREEN_END
            else -> TutorialStep.HOMESCREEN_END
        }
    )

public fun TutorialInformation.endHomeScreenTutorial(): TutorialInformation =
    copy (
        isActive = false,
        tutorialStep = TutorialStep.RECEIPTS_START
    )

public fun TutorialInformation.restartReceiptScreenTutorial(): TutorialInformation =
    copy (
        isActive = true,
        tutorialStep = TutorialStep.RECEIPTS_START
    )

public fun TutorialInformation.advanceReceiptScreenTutorial(): TutorialInformation =
    copy (
        isActive = true,
        tutorialStep = when (tutorialStep) {
            TutorialStep.RECEIPTS_START -> TutorialStep.RECEIPTS_TAKE_PICTURE
            TutorialStep.RECEIPTS_TAKE_PICTURE -> TutorialStep.RECEIPTS_LOG_SECTION
            TutorialStep.RECEIPTS_LOG_SECTION -> TutorialStep.RECEIPTS_SEE_METRICS_SECTION
            TutorialStep.RECEIPTS_SEE_METRICS_SECTION -> TutorialStep.RECEIPTS_SUM_SECTION
            TutorialStep.RECEIPTS_SUM_SECTION -> TutorialStep.RECEIPTS_END
            else -> TutorialStep.RECEIPTS_START
        }
    )

public fun TutorialInformation.endReceiptsScreenTutorial(): TutorialInformation =
    copy (
        isActive = false,
        tutorialStep = TutorialStep.GOALS_START
    )

public fun TutorialInformation.advanceGoalHistoryScreenTutorial(): TutorialInformation =
    copy (
        isActive = true,
        tutorialStep = when (tutorialStep) {
            TutorialStep.GOALS_START -> TutorialStep.GOALS_PUNCHCARD
            TutorialStep.GOALS_PUNCHCARD -> TutorialStep.GOALS_ACHIEVEMENTS
            TutorialStep.GOALS_ACHIEVEMENTS -> TutorialStep.GOALS_END
            else -> TutorialStep.GOALS_START
        }
    )

public fun TutorialInformation.endGoalHistoryScreenTutorial(): TutorialInformation =
    copy (
        isActive = false,
        tutorialStep = TutorialStep.GOALS_START
    )
