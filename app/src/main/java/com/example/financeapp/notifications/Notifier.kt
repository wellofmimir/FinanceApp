package com.example.financeapp.notifications

import com.example.financeapp.MainActivity
import com.example.financeapp.R
import com.example.financeapp.network.DailyTip

import android.Manifest
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Build

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat



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
            .setSmallIcon(R.mipmap.applogo_transparent_foreground)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setContentTitle("New quote available")
            .setContentText("A new quote is waiting for you.")
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

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity (
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminders")
            .setSmallIcon(R.mipmap.applogo_transparent_foreground)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setContentTitle("Check your receipt!")
            .setContentText("Hey, you wanted us to remind you of your receipt: $receiptName")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

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

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity (
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val notification = NotificationCompat.Builder(context, "tips")
            .setSmallIcon(R.mipmap.applogo_transparent_foreground)
            .setColor(ContextCompat.getColor(context, R.color.black))
            .setContentTitle("New financial tip available!")
            .setContentText(dailyTip.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

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
            .setSmallIcon(R.mipmap.applogo_round)
            .setContentTitle("Track your receipt")
            .setContentText("Any expenses today? Track the receipt!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(context).notify(1, notification.build())
    }
}