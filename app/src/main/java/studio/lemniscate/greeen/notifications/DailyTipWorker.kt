package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.repositories.DailyTipRepository

import android.content.Context
import androidx.work.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

            database.resetDailyTipObtained()

            val newTip = repository.fetchDailyTipFromServer()

            if (!newTip.title.contains("error")) {
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

        } catch (e: Exception) {
            Result.retry()
        }
    }
}