package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.SharedHttpClient
import com.example.financeapp.network.TrendClient
import com.example.financeapp.network.TrendRequest

class MetricsRepository private constructor (
    private val database: FinanceAppDatabase
) {
    private val client = TrendClient(SharedHttpClient.sharedClient)
    companion object {
        private var instance: MetricsRepository? = null

        fun getInstance(database: FinanceAppDatabase): MetricsRepository {
            if (instance == null)
                instance = MetricsRepository(database)

            return instance!!
        }
    }

    suspend fun getDailyTrend (
        trendRequest: TrendRequest
    ): String {
        val result = client.fetchTrend(trendRequest)

        if (result.text.isEmpty())
            return "No trend update today — something didn’t quite work, but we’ll try again shortly."

        return result.text
    }

    fun setRewardedAdAfterDailyTrendSeen() {
        database.setRewardedAdAfterDailyTrendSeen()
    }

    fun resetRewardedAdAfterDailyTrendSeen() {
        database.resetRewardedAdAfterDailyTrendSeen()
    }

    fun rewardedAdAfterDailyTrendSeen(): Boolean {
        return database.rewardedAdAfterDailyTrendSeen()
    }
}