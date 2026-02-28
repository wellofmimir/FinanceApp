package studio.lemniscate.greeen.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class QuoteAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        QuoteWorker.enqueue(context)
        QuoteAlarmScheduler.schedule(context)
    }
}