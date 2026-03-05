package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.repositories.DailyTipRepository

import android.content.Context
import androidx.work.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class DailyTipWorker (
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        fun enqueue(context: Context) {
            val request = OneTimeWorkRequestBuilder<DailyTipWorker>()
                .setConstraints (
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setBackoffCriteria (
                    BackoffPolicy.LINEAR,
                    5,
                    TimeUnit.MINUTES
                )
                .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork (
                    "daily_tip_exact",
                    ExistingWorkPolicy.KEEP,
                    request
                )
        }
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = FinanceAppDatabase.getInstance(applicationContext)
            val fileProvider = FileProvider(applicationContext)
            val repository = DailyTipRepository.getInstance(database, fileProvider)
            val notifier = Notifier(applicationContext)

            database.resetDailyTipAvailable()
            database.resetDailyTipObtained()
            DailyEvents.newDailyTip(false)

            val oldTip = repository.getDailyTip()
            val newTip = repository.fetchDailyTipFromServer()

            return@withContext if (newTip.title.contains("error") || newTip.tip.isEmpty()) {
                if (runAttemptCount >= 60)
                    Result.failure()
                else
                    Result.retry()
            } else {
                if (newTip.tip != oldTip.tip) {
                    database.setDailyTipAvailable()
                    database.setDailyTipObtained()
                    database.setDailyTip (
                        newTip.title,
                        newTip.tip,
                        newTip.short,
                        newTip.category,
                        newTip.pathToImage
                    )

                    DailyEvents.newDailyTip(true)
                    notifier.sendNewDailyTipAvailableNotification(newTip)
                }

                Result.success()
            }

        } catch (e: Exception) {
            if (runAttemptCount >= 60)
                Result.failure()
            else
                Result.retry()
        }
    }
}