package studio.lemniscate.greeen.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class TrendRequest (
    val category: String,
    val unit: String, //days, month, year
    val values: List<Float>
)
data class TrendResponse (
    val text: String
)

class TrendClient (
    private val client: OkHttpClient
) {
    suspend fun fetchTrend (
        trendRequest: TrendRequest
    ): TrendResponse = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonBody = Json.encodeToString(trendRequest)

        val request = Request.Builder()
            .addHeader("Content-Type", "application/json")
            .url("https://greeen-app.com/api/llm/trends/expenses")
            .post(jsonBody.toRequestBody(mediaType))
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                TrendResponse(responseBody)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            TrendResponse("")
        }
    }
}