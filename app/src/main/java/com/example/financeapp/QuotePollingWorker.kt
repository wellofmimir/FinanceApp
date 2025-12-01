package com.example.financeapp

import androidx.work.WorkerParameters
import android.content.Context
import androidx.compose.runtime.LaunchedEffect
import androidx.work.CoroutineWorker

class QuotePollingWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val quoteRepository = QuoteRepository.getInstance(database)

    private val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        database.resetDailyQuoteFetched()
        quoteRepository.fetchQuoteFromServer()
        notifier.sendQuoteNotification()
        return Result.success()
    }

}
