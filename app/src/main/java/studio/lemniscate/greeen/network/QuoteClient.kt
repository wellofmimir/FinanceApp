package studio.lemniscate.greeen.network

import okhttp3.OkHttpClient
import okhttp3.Request

class QuoteClient (
    private val client: OkHttpClient
) {
    var hasError: Boolean = false

    suspend fun fetchQuote(): String = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {

        val request = Request.Builder()
            .get()
            .url("https://greeen-app.com/api/quote")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                response.body?.string() ?: ""
            }
        } catch (e: Exception) {
            hasError = true
            ""
        }
    }
}