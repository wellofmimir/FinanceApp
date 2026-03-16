package studio.lemniscate.greeen.dailytipscreen

import android.app.Activity

import studio.lemniscate.greeen.repositories.DailyTipRepository
import studio.lemniscate.greeen.database.Tip
import studio.lemniscate.greeen.advertisement.RewardedAdManager
import studio.lemniscate.greeen.commonutils.fixOrientation
import studio.lemniscate.greeen.network.DailyTip
import studio.lemniscate.greeen.notifications.DailyEvents

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.File
import kotlin.collections.filter


class DailyTipScreenViewModel (
    private val rewardedAdManager: RewardedAdManager,
    private val repository: DailyTipRepository
): ViewModel() {

    private var internDailyTip = MutableStateFlow<Tip>(Tip(id = 0, DailyTip("Breathe in...", "", "Your article will be here shortly.", "", "")))
    var dailyTip = internDailyTip.asStateFlow()

    private var internLikedTips = MutableStateFlow<List<Tip>>(emptyList())
    var likedTips = internLikedTips.asStateFlow()

    private var internCurrentlyLiked = MutableStateFlow(false)
    val currentlyLiked = internCurrentlyLiked.asStateFlow()

    private val internImageToDailyTip = MutableStateFlow<Bitmap?>(null)
    val imageToDailyTip: StateFlow<ImageBitmap?> = internImageToDailyTip
        .map {
            it?.asImageBitmap()
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val internNewDailyTipAvailable = MutableStateFlow(true)
    val newDailyTipAvailable = internNewDailyTipAvailable.asStateFlow()

    init {
        viewModelScope.launch {
            DailyEvents.newDailyTipAvailable.collect() {
                internNewDailyTipAvailable.value = it
            }
        }
    }

    fun resetDailyTipAvailable() {
    }

    fun resetNewDailyTipAvailable() {
        internNewDailyTipAvailable.value = false
        repository.resetDailyTipAvailable()
    }

    fun isDailyTipLiked(dailyTip: DailyTip): Boolean {
        return internLikedTips.value.any { likedTip ->
            likedTip.dailyTip.tip == dailyTip.tip
        }
    }

    fun toggleDailyTipLiked(dailyTip: DailyTip) {
        viewModelScope.launch {
            if (dailyTip.title == "Breathe in...")
                return@launch

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

            if (dailyTipObtained()) {
                internDailyTip.value = internDailyTip.value.copy (
                    dailyTip = repository.getDailyTip()
                )

                internImageToDailyTip.value = BitmapFactory
                    .decodeFile(repository.getDailyTip().pathToImage)
                    ?.fixOrientation(repository.getDailyTip().pathToImage)

                return@launch
            }

            val dailyTip = repository.fetchDailyTipFromServer()

            if (dailyTip.title == "An error has occurred.")
                return@launch

            repository.setDailyTip(dailyTip)
            repository.setDailyTipObtained()
            DailyEvents.newDailyTip(true)

            internDailyTip.value = internDailyTip.value.copy (
                dailyTip = repository.getDailyTip()
            )

            if (repository.getDailyTip().pathToImage.isEmpty())
                return@launch

            internImageToDailyTip.value = BitmapFactory
                .decodeFile(repository.getDailyTip().pathToImage)
                ?.fixOrientation(repository.getDailyTip().pathToImage)

            internCurrentlyLiked.value = isDailyTipLiked(internDailyTip.value.dailyTip)
        }
    }

    fun getImageBitmapFromDailyTip(dailyTip: DailyTip): ImageBitmap {
        val imageBitmap = BitmapFactory
            .decodeFile(dailyTip.pathToImage)
            .fixOrientation(dailyTip.pathToImage)
            .asImageBitmap()

        return imageBitmap
    }

    fun dailyTipObtained(): Boolean {
        return repository.dailyTipObtained()
    }

    fun insertDailyTip(dailyTip: DailyTip) {
        repository.insertDailyTip(dailyTip)
    }

    fun removeDailyTip(dailyTip: DailyTip) {
        repository.removeDailyTip(dailyTip)
    }
    fun getLikedTips() {
        internLikedTips.value = repository.getLikedTips()
        val currentDailyTip = repository.getDailyTip()
        
        if (isDailyTipLiked(currentDailyTip))
            internCurrentlyLiked.value = true
    }

    var newDailyTipCanBeShown by mutableStateOf(false)
        private set

    fun onWatchAd (
        activity: Activity,
    ) {
        if (rewardedAdManager.isReady()) {
            rewardedAdManager.show (
                activity,
                onReward = {
                    repository.resetDailyTipAvailable()
                    newDailyTipCanBeShown = true
                },
                onClosed = {
                    rewardedAdManager.load("ca-app-pub-3940256099942544/5224354917")
                }
            )
        } else {
            newDailyTipCanBeShown = true
        }
    }
}