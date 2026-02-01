package com.example.financeapp.network

import okhttp3.OkHttpClient

object SharedHttpClient {
    val sharedClient = OkHttpClient()
}