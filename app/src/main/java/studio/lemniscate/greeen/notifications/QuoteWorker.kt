package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.repositories.QuoteRepository

import android.content.Context
import androidx.work.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuoteWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<QuoteWorker>()
                .setConstraints (
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork (
                    "quote_exact",
                    ExistingWorkPolicy.KEEP,
                    request
                )
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val database = FinanceAppDatabase.getInstance(applicationContext)
        val notifier = Notifier(applicationContext)

        database.resetDailyQuoteFetched()
        val oldQuote = database.dailyQuote()
        val newQuote = QuoteRepository.getInstance(database).fetchQuoteFromServer()

        if (newQuote.quote != oldQuote.first) {
            database.resetFeedbackSent()
            notifier.sendQuoteNotification()

            DailyEvents.newQuote(true)
        }

        Result.success()
    }
}
