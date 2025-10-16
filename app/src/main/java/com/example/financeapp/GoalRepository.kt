package com.example.financeapp

class GoalRepository(private val database: FinanceAppDatabase) {

    fun insertGoal(goal: Goal) {

        database.run {
            val goalStatus = getIDGoalStatus("Completed")!!
            insertGoal(goal.goal, goal.amount, goalStatus)
        }
    }

    fun getCompletedGoals(): List<Goal> {

       // val completedStatus = database.getIDGoalStatus("Completed")
         //   ?: return emptyList()

        return database.getGoals() //.filter{it.idStatus == completedStatus.id}
    }
}