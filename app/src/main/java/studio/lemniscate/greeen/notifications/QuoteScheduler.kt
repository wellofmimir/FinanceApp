package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.BuildConfig


import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

import java.util.Calendar
import java.util.concurrent.TimeUnit

object QuoteScheduler {

    fun schedule(context: Context) {

        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {

            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now))
                add(Calendar.DAY_OF_MONTH, 1)
        }

        val initialDelay = next.timeInMillis - now.timeInMillis

        val workRequest = OneTimeWorkRequestBuilder<QuotePollingWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setBackoffCriteria (
                androidx.work.BackoffPolicy.LINEAR,
                1,
                TimeUnit.HOURS
            )
            .build()

        val workManager = WorkManager.getInstance(context)

        if (BuildConfig.DEBUG)
            workManager.cancelUniqueWork("dailyQuoteWorker")

        workManager.enqueueUniqueWork (
            "dailyQuoteWorker",
            ExistingWorkPolicy.KEEP,
            workRequest
        )
    }
}
