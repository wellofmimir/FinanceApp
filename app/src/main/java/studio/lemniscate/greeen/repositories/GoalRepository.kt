package studio.lemniscate.greeen.repositories

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.database.Goal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class GoalRepository private constructor (private val database: FinanceAppDatabase) {

    companion object {
        private var instance: GoalRepository? = null

        fun getInstance(database: FinanceAppDatabase): GoalRepository {
            if (instance == null)
                instance = GoalRepository(database)

            return instance!!
        }
    }

    fun insertGoal(goal: String, amount: Float, saved: Float, statusDescription: String, amountOfTokens: Int) {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        database.run {
            insertGoal(goal, amount, saved,statusDescription, amountOfTokens, formattedDate)
        }
    }

    fun updateImageToGoal(idGoal: Int, pathToImage: String) {
        database.run {
            updateImageToGoal(idGoal, pathToImage)
        }
    }
    fun insertGoal(goal: Goal) {
        database.run {
            insertGoal(goal)
        }
    }

    fun deleteGoal(goal: Goal) {
        database.run {
            deleteGoal(goal)
        }
    }

    fun deleteGoal(id: Int) {
        database.run {
            deleteGoal(id)
        }
    }


    fun getNewestGoalId(): Int {
        return database.getNewestGoalId()
    }

    fun updateGoal(goal: Goal) {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        goal.dateWhenFinished = formattedDate
        database.updateGoal(goal)
    }

    fun setGoalCompleted(goal: Goal) {
        database.run {
            val status = getIDGoalStatus("Completed")

            if (goal.idStatus == status!!.id)
                return

            goal.idStatus = status.id
            updateGoal(goal)
            addNewTokenAmountToPunchcard(goal.tokenCount)
        }
    }

    fun getInProgressGoals(): List<Goal> {
        val inProgressStatus = database.getIDGoalStatus("InProgress")
            ?: return emptyList()

        return database.getGoals().filter {
            it.idStatus == inProgressStatus.id
        }
    }

    fun getCompletedGoals(): List<Goal> {
        val completedStatus = database.getIDGoalStatus("Completed") ?:
            return emptyList()

        val punchCardStatus = database.getIDGoalStatus("PunchCard") ?:
            return emptyList()

        return database.getGoals().filter {
            it.idStatus == completedStatus.id || it.idStatus == punchCardStatus.id
        }
    }

    fun getCompletedGoalsOrderedRandomly(): List<Goal> {
        val completedStatus = database.getIDGoalStatus("Completed") ?:
            return emptyList()

        val punchCardStatus = database.getIDGoalStatus("PunchCard") ?:
            return emptyList()

        return database.getGoalsOrderedRandomly().filter {
            it.idStatus == completedStatus.id || it.idStatus == punchCardStatus.id
        }
    }

    fun getCurrentGoal(): Goal? {
        val goal = database.currentGoal()

        if (goal == null)
            return null

        val completedStatus = database.getIDGoalStatus("Completed")

        if (goal.idStatus == completedStatus!!.id)
            return null

        return goal
    }

    fun setCurrentGoal(goal: Goal) {
        return database.setCurrentGoal(goal)
    }

    fun getTokenSoFarForPunchcard(): Int {
        return database.getTokenSoFarForPunchcard()
    }

    fun resetPunchcard() {
        database.resetPunchcard()
    }
    fun addNewTokenAmountToPunchcard(tokenAmount: Int) {
        database.setTokenSoFarForPunchcard(tokenAmount)
    }
    fun resetTokenSoFarForPunchcard() {
        database.setTokenSoFarForPunchcard(0)
    }

    fun addToTotalTokensEarned(amount: Int) {
        database.addTotalTokensEarned(amount)
    }
    fun getTotalTokensEarned(): Int {
        return database.getTotalTokensEarned()
    }

    fun firstGoalAddedDone(): Boolean {
        return database.firstGoalAddedDone()
    }

    fun setFirstGoalAddedDone() {
        database.setFirstGoalAddedDone()
    }

    fun firstGoalEditingDone(): Boolean {
        return database.firstGoalEditingDone()
    }

    fun setFirstGoalEditingDone() {
        database.setFirstGoalEditingDone()
    }
}