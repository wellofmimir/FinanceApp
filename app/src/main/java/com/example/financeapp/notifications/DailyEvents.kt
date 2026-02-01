package com.example.financeapp.notifications

import com.example.financeapp.database.Quote
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DailyEvents {
    private val internNewDailyTipAvailable = MutableSharedFlow<Boolean> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val newDailyTipAvailable = internNewDailyTipAvailable.asSharedFlow()

    fun newDailyTip(newDailyTipAvailable: Boolean) {
        internNewDailyTipAvailable.tryEmit(newDailyTipAvailable)
    }

    private val internNewQuoteAvailable = MutableSharedFlow<Boolean> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val newQuoteAvailable = internNewQuoteAvailable.asSharedFlow()

    fun newQuote(newQuoteAvailable: Boolean) {
        internNewQuoteAvailable.tryEmit(newQuoteAvailable)
    }
}