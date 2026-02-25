package studio.lemniscate.greeen

import android.app.Application
import studio.lemniscate.greeen.notifications.DailyTipAlarmScheduler
import studio.lemniscate.greeen.notifications.QuoteScheduler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyTipAlarmScheduler.schedule(this)
        QuoteScheduler.schedule(this)
    }
}