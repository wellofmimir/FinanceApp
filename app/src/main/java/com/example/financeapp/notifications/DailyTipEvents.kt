package com.example.financeapp.notifications

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DailyTipEvents {

    private val internNewDailyTipAvailable = MutableSharedFlow<Boolean> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val newDailyTipAvailable = internNewDailyTipAvailable.asSharedFlow()

    fun newDailyTip(newDailyTipAvailable: Boolean) {
        internNewDailyTipAvailable.tryEmit(newDailyTipAvailable)
    }
}