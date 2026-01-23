package com.example.financeapp.notifications

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.commonutils.FileProvider
import com.example.financeapp.repositories.DailyTipRepository

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyTipWorker (
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker (
    appContext = context,
    params = parameters
) {
    private val fileProvider = FileProvider(applicationContext)
    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val dailyTipRepository = DailyTipRepository.getInstance(database, fileProvider)
    private  val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        val oldDailyTip = database.getDailyTip()
        database.setDailyTip("", "", "", "", "")
        database.resetInterstitialAdAfterDailyTip()
        val newDailyTip = dailyTipRepository.fetchDailyTipFromServer()

        if (newDailyTip.tip.isNotEmpty() && newDailyTip.title.isNotEmpty() && newDailyTip.title.hashCode() != oldDailyTip.title.hashCode()) {
            notifier.sendNewDailyTipAvailableNotification(dailyTipRepository.getDailyTip())
            DailyTipEvents.newDailyTip(true)
        } else {
            DailyTipScheduler.scheduleRetry(applicationContext)
        }

        return Result.success()
    }
}