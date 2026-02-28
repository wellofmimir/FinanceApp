package studio.lemniscate.greeen.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DailyTipAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        DailyTipWorker.enqueue(context)
        DailyTipAlarmScheduler.schedule(context)
    }
}