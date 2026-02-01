package com.example.financeapp.notifications

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.QuoteRepository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class QuotePollingWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val quoteRepository = QuoteRepository.getInstance(database)
    private val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        //Hier kann auch direkt das Feedback zurückgesetzt werden
        //Erstmal ist dafür kein eigener Worker notwendig

        if (database.getQuoteTryCounter() >= 16) {
            database.resetQuoteTryCounter()
            return Result.success()
        }

        database.resetInterstitialAdAfterReceiptSeen()
        database.resetDailyQuoteFetched()

        val oldQuote = database.dailyQuote()
        val newQuote = quoteRepository.fetchQuoteFromServer()

        if (newQuote.quote != oldQuote.first) {
            database.resetFeedbackSent()
            database.resetQuoteTryCounter()
            notifier.sendQuoteNotification()
            DailyEvents.newQuote(true)
        }
        else {
            database.incrementQuoteTryCounter()
            QuoteScheduler.scheduleRetry(applicationContext)
        }

        return Result.success()
    }
}