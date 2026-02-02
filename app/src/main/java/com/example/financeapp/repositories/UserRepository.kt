package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase

class UserRepository private constructor (private val database: FinanceAppDatabase) {
    companion object {
        private var instance: UserRepository? = null

        fun getInstance(database: FinanceAppDatabase): UserRepository {
            if (instance == null)
                instance = UserRepository(database)

            return instance!!
        }
    }

    fun setCurrentTheme(theme: String) {
        database.setCurrentTheme(theme)
    }

    fun getCurrentTheme(): String {
        return database.getCurrentTheme()
    }

    fun getUser(): String {
        return database.getUser()
    }
    fun updateUser(user: String) {
        database.updateUser(user)
    }
    fun getHomeScreenTutorialDone(): Boolean {
        return database.getHomeScreenTutorialDone()
    }
    fun setHomeScreenTutorialDone() {
        database.setHomeScreenTutorialDone()
    }
    fun resetHomeScreenTutorialDone() {
        database.resetHomeScreenTutorialDone()
    }
    fun getReceiptsTutorialDone(): Boolean {
        return database.getReceiptsTutorialDone()
    }
    fun setReceiptsTutorialDone() {
        database.setReceiptsTutorialDone()
    }
    fun resetReceiptsTutorialDone() {
        database.resetReceiptsTutorialDone()
    }
    fun setCurrency(currency: String) {
        database.setCurrency(currency)
    }

    fun getGoalHistoryTutorialDone(): Boolean {
        return database.getGoalHistoryTutorialDone()
    }

    fun setGoalHistoryTutorialDone() {
        database.setGoalHistoryTutorialDone()
    }

    fun resetGoalHistoryTutorialDone() {
        database.resetGoalHistoryTutorialDone()
    }
}