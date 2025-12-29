package com.example.financeapp.repositories

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.FeedbackClient
import com.example.financeapp.network.FeedbackClientCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val internFeedbackAlreadySent = MutableStateFlow(false)
    val isFeedbackAlreadySent = internFeedbackAlreadySent.asStateFlow()

    private val client = FeedbackClient.getInstance()
    fun sendFeedback(name: String, text: String) {

        if (database.feedbackAlreadySent()) {
            return
        }

        val result = client.sendFeedback(name, text,object: FeedbackClientCallback {
            override fun result(response: String) {

                val jsonObject = JSONObject(response)

                if (jsonObject.getString("status") == "0") {
                    database.setFeedbackSent()
                    feedbackAlreadySent()
                } else {
                    database.resetFeedbackSent()
                }
            }
        })
    }

    fun feedbackAlreadySent(): Boolean {
        internFeedbackAlreadySent.value = database.feedbackAlreadySent()
        return internFeedbackAlreadySent.value
    }
}