package com.example.financeapp.network

import okhttp3.OkHttpClient
import okhttp3.Request

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

interface FeedbackClientCallback {
    fun result(response: String)
}

data class Feedback (
    val name: String,
    val text: String
)

class FeedbackClient private constructor() {
    companion object {
        private var instance: FeedbackClient? = null

        fun getInstance(): FeedbackClient {
            if (instance == null)
                instance = FeedbackClient()

            return instance!!
        }
    }

    private val client = OkHttpClient()

    fun sendFeedback(name: String, text: String, callback: FeedbackClientCallback) {

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val jsonAsRequestBody = JSONObject().apply {
            put("name", name)
            put ("text", text)

        }.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://shortlyfi.me/api/feedback")
            .addHeader("Content-Type", "application/json")
            .post(jsonAsRequestBody)
            .build()

        Thread {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                response.close()

                Handler (Looper.getMainLooper()).post {
                    callback.result(responseBody!!)
                }

            } catch (e: kotlin.Exception) {
                Handler (Looper.getMainLooper()).post {
                    callback.result("")
                }
            }
        }.start()
    }
}