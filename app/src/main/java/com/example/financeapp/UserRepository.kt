package com.example.financeapp

class UserRepository(private val database: FinanceAppDatabase) {

    fun getUser(): String {
        return database.getUser()
    }

    fun updateUser(user: String) {
        database.updateUser(user)
    }

    fun isTutorialDone(): Boolean {
        return database.isTutorialDone()
    }

    fun updateTutorialDoneStatus(status: Boolean) {
        return database.updateTutorialStatus(if (status) 1 else 0)
    }
}