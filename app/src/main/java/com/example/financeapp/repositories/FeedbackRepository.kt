package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.FeedbackClient
import org.json.JSONObject

class FeedbackRepository private constructor (private val database: FinanceAppDatabase) {
    companion object {
        private var instance: FeedbackRepository? = null

        fun getInstance(database: FinanceAppDatabase): FeedbackRepository {
            if (instance == null)
                instance = FeedbackRepository(database)

            return instance!!
        }
    }

    private val client = FeedbackClient.getInstance()

    fun feedbackAlreadySent(): Boolean {
        return database.feedbackAlreadySent()
    }

    suspend fun sendFeedback(name: String, text: String) {

        if (database.feedbackAlreadySent())
            return

        val result = client.sendFeedback(name, text)
        val jsonObject = JSONObject(result)

        if (jsonObject.getString("status") == "0")
            database.setFeedbackSent()
        else
            database.resetFeedbackSent()
    }
}