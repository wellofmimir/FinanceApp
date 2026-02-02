package studio.lemniscate.greeen.repositories

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.network.SharedHttpClient
import studio.lemniscate.greeen.network.TrendClient
import studio.lemniscate.greeen.network.TrendRequest

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

    fun getOldDailyTrend(): String {
        return database.getDailyTrend()
    }

    fun setRewardedAdAfterDailyTrendSeen(trendText: String) {
        database.setRewardedAdAfterDailyTrendSeen(trendText)
    }

    fun rewardedAdAfterDailyTrendSeen(): Boolean {
        return database.rewardedAdAfterDailyTrendSeen()
    }
}