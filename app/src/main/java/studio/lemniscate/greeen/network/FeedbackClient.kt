package studio.lemniscate.greeen.network

import okhttp3.OkHttpClient
import okhttp3.Request

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class FeedbackClient (
    private val client: OkHttpClient
) {


    suspend fun sendFeedback(name: String, text: String): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val jsonRequestBody = JSONObject().apply {
            put("name", name)
            put("text", text)
        }.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://greeen-app.com/api/feedback")
            .addHeader("Content-Type", "application/json")
            .post(jsonRequestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.string() ?: ""
            }
        } catch (e: Exception) {
            ""
        }
    }
}