package com.example.financeapp.dailytipscreen
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.financeapp.repositories.DailyTipRepository
import com.example.financeapp.database.Tip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.commonutils.fixOrientation
import com.example.financeapp.network.DailyTip
import com.example.financeapp.notifications.DailyTipEvents
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DailyTipScreenViewModel(private val repository: DailyTipRepository): ViewModel() {

    private var internDailyTip = MutableStateFlow<Tip>(Tip(id = 0, DailyTip("", "", "", "", "")))
    var dailyTip = internDailyTip.asStateFlow()

    private var internLikedTips = MutableStateFlow<List<Tip>>(emptyList())
    var likedTips = internLikedTips.asStateFlow()

    private var internLikedTipsRandomlyOrdered = MutableStateFlow<List<Tip>>(emptyList())
    var likedTipsRandomlyOrdered = internLikedTipsRandomlyOrdered.asStateFlow()

    private var internCurrentlyLiked = MutableStateFlow(false)
    val currentlyLiked = internCurrentlyLiked.asStateFlow()

    private var internImageToDailyTip = MutableStateFlow<Bitmap?>(null)
    val imageToDailyTip = internImageToDailyTip.asStateFlow()

    private val internNewDailyTipAvailable = MutableStateFlow(getNewDailyTipAvailable())
    val newDailyTipAvailable = internNewDailyTipAvailable.asStateFlow()

    init {
        viewModelScope.launch {
            DailyTipEvents.newDailyTipAvailable.collect() {
                internNewDailyTipAvailable.value = true
            }
        }
    }

    fun resetNewDailyTipAvailable() {
        repository.resetNewDailyTipAvailable()
        internNewDailyTipAvailable.value = false
    }

    fun getNewDailyTipAvailable(): Boolean {
        return repository.newDailyTipAvailable()
    }

    fun interstitialAdAfterDailyTipSeen(): Boolean {
        return repository.interstitialAdAfterDailyTipSeen()
    }

    fun setInterstitialAdAfterDailyTipSeen() {
        repository.setInterstitialAdAfterDailyTipSeen()
    }

    fun isDailyTipLiked(dailyTip: DailyTip): Boolean {
        return internLikedTips.value.any { likedTip ->
            likedTip.dailyTip.tip == dailyTip.tip
        }
    }

    fun toggleDailyTipLiked(dailyTip: DailyTip) {
        viewModelScope.launch {
            if (isDailyTipLiked(dailyTip)) {
                removeDailyTip(dailyTip)
                internCurrentlyLiked.value = false
            } else {
                insertDailyTip(dailyTip)
                internCurrentlyLiked.value = true
            }

            internLikedTips.value = repository.getLikedTips()
        }
    }

    fun fetchDailyTip() {
        viewModelScope.launch {
            repository.fetchDailyTipFromServer()

            internDailyTip.value = internDailyTip.value.copy (
                dailyTip = repository.getDailyTip()
            )

            internImageToDailyTip.value = BitmapFactory
                .decodeFile(repository.getDailyTip().pathToImage)
                .fixOrientation(repository.getDailyTip().pathToImage)

            internCurrentlyLiked.value = isDailyTipLiked(internDailyTip.value.dailyTip)
        }
    }

    fun insertDailyTip(dailyTip: DailyTip) {
        repository.insertDailyTip(dailyTip)
    }

    fun removeDailyTip(dailyTip: DailyTip) {
        repository.removeDailyTip(dailyTip)
    }
    fun getLikedTips() {
        internLikedTips.value = repository.getLikedTips()
    }

    fun getLikedTipsOrderedRandomly(): List<Tip> {
        internLikedTipsRandomlyOrdered.value = repository.getLikedTipsRandomlyOrdered()
        return internLikedTipsRandomlyOrdered.value
    }
}