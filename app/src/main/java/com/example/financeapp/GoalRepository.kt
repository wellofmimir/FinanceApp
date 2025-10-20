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

    fun updateGoal(goal: Goal) {

        database.run {
            updateGoal(goal)
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

        if (goal == null)
            return Goal(1, "Test", 1000.0f, 120.0f, 1)

        return goal
    }

    fun setCurrentGoal(goal: Goal) {

        return database.setCurrentGoal(goal)
    }
}