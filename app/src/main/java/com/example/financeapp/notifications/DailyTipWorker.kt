package com.example.financeapp.notifications

import com.example.financeapp.database.FinanceAppDatabase

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.financeapp.commonutils.FileProvider
import com.example.financeapp.repositories.DailyTipRepository

class DailyTipWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    private val fileProvider = FileProvider(applicationContext)
    private val database = FinanceAppDatabase.getInstance(applicationContext)
    private val dailyTipRepository = DailyTipRepository.getInstance(database, fileProvider)
    private  val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {

        database.setDailyTip("", "", "", "", "")
        database.resetInterstitialAdAfterDailyTip()
        dailyTipRepository.fetchDailyTipFromServer()

        val dailyTip = dailyTipRepository.getDailyTip()

        if (dailyTip.tip.isNotEmpty() && dailyTip.title.isNotEmpty()) {
            notifier.sendNewDailyTipAvailableNotification(dailyTipRepository.getDailyTip())
            DailyTipEvents.newDailyTip(true)
        }

        return Result.success()
    }
}