package studio.lemniscate.greeen.notifications

import android.content.Context
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object RemindMeScheduler {

    fun schedule(context: Context) {

        val now = Calendar.getInstance()
        val nextReminder = Calendar.getInstance().apply {

            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 10)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now))
                add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = nextReminder.timeInMillis - now.timeInMillis

        val workRequest = PeriodicWorkRequestBuilder<ReceiptReminderPollingWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork (
            "dailyReminderWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleRetry(context: Context) {

        val retryWork = OneTimeWorkRequestBuilder<DailyTipWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork (
            "dailyReminderRetry",
            ExistingWorkPolicy.REPLACE,
            retryWork
        )
    }
}