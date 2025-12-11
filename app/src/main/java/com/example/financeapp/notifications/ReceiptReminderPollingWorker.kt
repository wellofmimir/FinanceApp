package com.example.financeapp.notifications

import com.example.financeapp.database.FinanceAppDatabase

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


class ReceiptReminderPollingWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        //Es wird die receiptRemindDates-Tabelle der Datenbank durchsucht
        //und für jeden gefundenen Eintrag, der mit dem heutigen Eintrag übereinstimmt,
        //eine Notification abgesendet.

        //1) alle Einträge aus Datenbank holen aus der Tabelle 'receiptRemindDates'
        val remindMeList = database.getReceiptRemindMe()

        //2) Das heutige Datum ermittelt
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        //3) Liste iterieren und schauen, ob ein Entry dabei ist, welches das aktuelle Datum als Erinnerungstermin hat -> Benachrichtigung schicken

        remindMeList.forEach { entry ->
            if (entry.date == formattedDate) {
                val receipt = database.getReceipt(entry.idReceipt)
                notifier.sendReceiptReminderNotification(receipt.description)
            }
        }

        return Result.success()
    }
}