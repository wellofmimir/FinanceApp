package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase

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

    fun setCurrency(currency: String) {
        database.setCurrency(currency)
    }
}