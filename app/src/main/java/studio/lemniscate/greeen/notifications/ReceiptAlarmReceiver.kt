package studio.lemniscate.greeen.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
class ReceiptAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        ReceiptWorker.enqueue(context)
        ReceiptAlarmScheduler.schedule(context)
    }
}