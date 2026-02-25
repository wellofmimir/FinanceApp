package studio.lemniscate.greeen.notifications

import studio.lemniscate.greeen.repositories.DailyTipRepository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.database.FinanceAppDatabase

class DailyTipAlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.IO).launch {

            val database = FinanceAppDatabase.getInstance(context)
            val fileProvider = FileProvider(context)
            val repository = DailyTipRepository.getInstance(database, fileProvider)
            val notifier = Notifier(context)

            database.resetDailyTipObtained()
            val oldTip = database.getDailyTip()
            val newTip = repository.fetchDailyTipFromServer()

            if (newTip.tip != oldTip.tip) {
                notifier.sendNewDailyTipAvailableNotification(newTip)
                DailyEvents.newDailyTip(true)
                database.setDailyTipAvailable()
            }

            DailyTipAlarmScheduler.schedule(context)
        }
    }
}