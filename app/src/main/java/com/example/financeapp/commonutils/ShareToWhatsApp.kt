package com.example.financeapp.commonutils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

fun getShareableImageUri(context: Context, path: String): Uri {

    val file = File(path)
    return FileProvider.getUriForFile(context, "com.example.financeapp.provider", file)
}
fun shareToWhatsapp(context: Context, imageUri: Uri, text: String) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        putExtra(Intent.EXTRA_TEXT, text)
        setPackage("com.whatsapp")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
    }
}