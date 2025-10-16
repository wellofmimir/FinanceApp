package com.example.financeapp

class GoalRepository(private val database: FinanceAppDatabase) {

    fun insertGoal(goal: String, amount: Float, statusDescription: String) {
        database.run {
            insertGoal(goal, amount, statusDescription)
        }
    }

    fun insertGoal(goal: Goal) {
        database.run {
            insertGoal(goal)
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
}