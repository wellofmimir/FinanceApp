package com.example.financeapp.notifications

import com.example.financeapp.database.FinanceAppDatabase

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.financeapp.repositories.DailyTipRepository

class DailyTipWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val dailyTipRepository = DailyTipRepository.getInstance(database)
    private  val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        database.resetNewDailyTipAvailable()
        database.setDailyTip("", "")
        database.resetInterstitialAdAfterDailyTip()

        dailyTipRepository.fetchDailyTipFromServer()
        return Result.success()
    }
}