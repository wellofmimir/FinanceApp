package com.example.financeapp

class UserRepository(private val database: FinanceAppDatabase) {

    fun getUser(): String {
        return database.getUser()
    }

    fun updateUser(user: String) {
        database.updateUser(user)
    }
}