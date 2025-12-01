package com.example.financeapp

import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationCompat
import android.os.Build
import android.content.Context

class Notifier(private val context: Context) {

    fun sendQuoteNotification() {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, "quotes")
            .setSmallIcon(R.drawable.starfilledpistachio_foreground)
            .setContentTitle("New quote available")
            .setContentText("A new motivational quote is waiting for you ...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

    fun sendReceiptNotification() {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, "receipts")
            .setSmallIcon(R.drawable.starfilledpistachio_foreground)
            .setContentTitle("Track your receipt")
            .setContentText("Hey, do you want to track your latest receipt?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }


}