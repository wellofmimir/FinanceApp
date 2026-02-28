package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.repositories.QuoteRepository

import android.content.Context
import androidx.work.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReceiptWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<ReceiptWorker>()
                .setConstraints (
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork (
                    "receipt_remind_me_exact",
                    ExistingWorkPolicy.KEEP,
                    request
                )
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val database = FinanceAppDatabase.getInstance(applicationContext)
        val notifier = Notifier(applicationContext)

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

        Result.success()
    }
}
