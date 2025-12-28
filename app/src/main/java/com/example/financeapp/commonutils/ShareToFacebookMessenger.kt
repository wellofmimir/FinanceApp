package com.example.financeapp.commonutils

import android.content.ActivityNotFoundException
import android.net.Uri
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun shareToFacebookMessenger(context: Context, imageUri: Uri) {

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, imageUri)
        setPackage("com.facebook.orca")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Facebook-Messenger is not installed.", Toast.LENGTH_SHORT).show()
    }
}