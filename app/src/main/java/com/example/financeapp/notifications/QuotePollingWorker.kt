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

        database.resetDailyQuoteFetched()
        database.resetFeedbackSent()
        database.resetInterstitialAdAfterReceiptSeen()

        val newQuote = quoteRepository.fetchQuoteFromServer()
        val currentQuote = database.dailyQuote()

        if (newQuote.quote != currentQuote.first)
            notifier.sendQuoteNotification()

        return Result.success()
    }

}