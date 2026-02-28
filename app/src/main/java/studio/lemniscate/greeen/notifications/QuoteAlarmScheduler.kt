package studio.lemniscate.greeen.notifications

import java.util.Calendar

import android.content.Context
import android.content.Intent

import android.app.AlarmManager
import android.app.PendingIntent

object QuoteAlarmScheduler {
    private const val REQUEST_CODE_ID = 1002

    fun schedule(context: Context) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, QuoteAlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast (
            context,
            REQUEST_CODE_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val now = Calendar.getInstance()
        val triggerTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(now))
                add(Calendar.DAY_OF_MONTH, 1)
        }

        alarmManager.setAndAllowWhileIdle (
            AlarmManager.RTC_WAKEUP,
            triggerTime.timeInMillis,
            pendingIntent
        )
    }
}