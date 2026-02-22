package studio.lemniscate.greeen

import android.app.Application
import studio.lemniscate.greeen.notifications.DailyTipScheduler

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DailyTipScheduler.schedule(this)
    }
}