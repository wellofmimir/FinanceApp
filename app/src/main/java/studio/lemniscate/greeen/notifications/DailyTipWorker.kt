package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.repositories.DailyTipRepository

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
    private val notifier = Notifier(applicationContext)

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 16) {
            DailyTipScheduler.schedule(applicationContext)
            return Result.success()
        }

        database.resetDailyTipObtained()
        val oldDailyTip = database.getDailyTip()
        val newDailyTip = dailyTipRepository.fetchDailyTipFromServer()

        if (newDailyTip.tip.isNotEmpty() &&
            newDailyTip.title.isNotEmpty() &&
            newDailyTip.title.hashCode() != oldDailyTip.title.hashCode()) {

            notifier.sendNewDailyTipAvailableNotification(newDailyTip)

            DailyEvents.newDailyTip(true)
            DailyTipScheduler.schedule(applicationContext)
            return Result.success()
        }

        return Result.retry()
    }
}
