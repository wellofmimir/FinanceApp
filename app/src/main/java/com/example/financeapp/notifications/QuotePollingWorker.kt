package com.example.financeapp.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.repositories.QuoteRepository

class QuotePollingWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    private val database = FinanceAppDatabase.Companion.getInstance(applicationContext)
    private val quoteRepository = QuoteRepository.Companion.getInstance(database)
    private val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        //Hier kann auch direkt das Feedback zurückgesetzt werden
        //Erstmal ist dafür kein eigener Worker notwendig

        database.resetDailyQuoteFetched()
        database.resetFeedbackSent()

        quoteRepository.fetchQuoteFromServer()

        val newQuote = quoteRepository.quoteToQuotedPerson.value
        val currentQuote = database.dailyQuote()

        if (newQuote.first != currentQuote.first)
            notifier.sendQuoteNotification()



        return Result.success()
    }

}