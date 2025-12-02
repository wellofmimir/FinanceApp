package com.example.financeapp

class GoalRepository(private val database: FinanceAppDatabase) {

    fun insertGoal(goal: String, amount: Float, saved: Float, statusDescription: String) {
        database.run {
            insertGoal(goal, amount, saved,statusDescription)
        }
    }

    fun insertGoal(goal: Goal) {
        database.run {
            insertGoal(goal)
        }
    }

    fun getNewestGoalId(): Int {
        return database.getNewestGoalId()
    }

    fun updateGoal(goal: Goal) {
        database.updateGoal(goal)
    }

    fun setGoalCompleted(goal: Goal) {
        database.run {
            val status = getIDGoalStatus("Completed")

            if (goal.idStatus == status!!.id)
                return

            goal.idStatus = status!!.id
            updateGoal(goal)
            addNewTokenAmountToPunchcard(goal.tokenCount)
        }
    }

    fun getInProgressGoals(): List<Goal> {
        val inProgressStatus = database.getIDGoalStatus("InProgress")
            ?: return emptyList()

        return database.getGoals().filter{it.idStatus == inProgressStatus.id}
    }

    fun getCompletedGoals(): List<Goal> {
       val completedStatus = database.getIDGoalStatus("Completed")
           ?: return emptyList()

        return database.getGoals().filter{it.idStatus == completedStatus.id}
    }

    fun getCurrentGoal(): Goal? {

        val goal = database.currentGoal()

        if (goal == null) {

            val currentDate = java.time.LocalDate.now()
            val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy", java.util.Locale.ENGLISH)
            val formattedDate = currentDate.format(formatter)

            return Goal(1, "Test", 1000.0f, 120.0f, 1, formattedDate, 3)
        }

        return goal
    }

    fun setCurrentGoal(goal: Goal) {
        return database.setCurrentGoal(goal)
    }

    fun getTokenSoFarForPunchcard(): Int {
        return database.getTokenSoFarForPunchcard()
    }

    fun addNewTokenAmountToPunchcard(tokenAmount: Int) {
        database.setTokenSoFarForPunchcard(tokenAmount)
    }
    fun resetTokenSoFarForPunchcard() {
        database.setTokenSoFarForPunchcard(0)
    }
}