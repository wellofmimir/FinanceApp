package studio.lemniscate.greeen.network

import okhttp3.OkHttpClient

object SharedHttpClient {
    val sharedClient = OkHttpClient()
}