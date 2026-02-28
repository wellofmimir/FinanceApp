package studio.lemniscate.greeen

import android.app.Application

import studio.lemniscate.greeen.notifications.DailyTipAlarmScheduler
import studio.lemniscate.greeen.notifications.QuoteAlarmScheduler
import studio.lemniscate.greeen.notifications.ReceiptAlarmScheduler
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyTipAlarmScheduler.schedule(this)
        QuoteAlarmScheduler.schedule(this)
        ReceiptAlarmScheduler.schedule(this)
    }
}