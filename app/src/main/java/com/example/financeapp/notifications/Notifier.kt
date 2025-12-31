package com.example.financeapp.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.financeapp.MainActivity
import com.example.financeapp.R
import com.example.financeapp.network.DailyTip

class Notifier(private val context: Context) {
    fun sendQuoteNotification() {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity (
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "quotes")
            .setSmallIcon(R.drawable.dollarsign_foreground)
            .setContentTitle("New quote available")
            .setContentText("A new quote is waiting for you ...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(1, notification.build())
    }

    fun sendReceiptReminderNotification(receiptName: String) {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission (
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, "reminders")
            .setSmallIcon(R.drawable.dollarsign_foreground)
            .setContentTitle("Check your receipt!")
            .setContentText("Hey, you wanted us to remind you of your receipt: ${receiptName}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationId = System.currentTimeMillis().toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification.build())
    }

    fun sendNewDailyTipAvailableNotification(dailyTip: DailyTip) {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission (
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, "tips")
            .setSmallIcon(R.drawable.dollarsign_foreground)
            .setContentTitle("New financial tip available!")
            .setContentText("A teaser ;) It's about: ${dailyTip.title}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationId = System.currentTimeMillis().toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification.build())
    }

    fun sendReceiptNotification() {

        if (Build.VERSION.SDK_INT >= 33 &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, "receipts")
            .setSmallIcon(R.drawable.dollarsign_foreground)
            .setContentTitle("Track your receipt")
            .setContentText("Hey, do you want to track your latest receipt?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(1, notification.build())
    }
}