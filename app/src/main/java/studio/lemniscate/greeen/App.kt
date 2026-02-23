package studio.lemniscate.greeen

import android.app.Application
import studio.lemniscate.greeen.notifications.DailyTipScheduler
import studio.lemniscate.greeen.notifications.QuoteScheduler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyTipScheduler.schedule(this)
        QuoteScheduler.schedule(this)
    }
}