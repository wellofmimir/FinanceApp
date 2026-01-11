package com.example.financeapp.commonutils

import java.io.File
import android.content.Context

class FileProvider (
    private val context: Context
) {
    fun getPNG(name: String = ""): File {
        return File(context.cacheDir, name.ifEmpty { "image_${System.currentTimeMillis()}.png" })
    }
}