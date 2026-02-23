package studio.lemniscate.greeen.notifications

import android.content.Context
import android.icu.util.Calendar
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object DailyTipScheduler {
    fun schedule(context: Context) {

        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now))
                add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<DailyTipWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setBackoffCriteria (
                androidx.work.BackoffPolicy.LINEAR,
                2,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork (
            "dailyTipWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
