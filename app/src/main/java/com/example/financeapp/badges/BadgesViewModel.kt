package com.example.financeapp.badges

import com.example.financeapp.repositories.BadgesRepository
import com.example.financeapp.database.Badge
import com.example.financeapp.commonutils.fixOrientation

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class BadgesViewModel (
    private val repository: BadgesRepository
): ViewModel() {

    private val internBadgeAvailable = MutableStateFlow(false)
    val isBadgeAvailable = internBadgeAvailable.asStateFlow()

    private val internUserBadges = MutableStateFlow<List<Badge>>(emptyList())
    val userBadges = internUserBadges.asStateFlow()

    private val internToastEvent = MutableSharedFlow<String>()
    val toastEvent = internToastEvent.asSharedFlow()

    suspend fun showToast(message: String) {
        internToastEvent.emit(message)
    }

    private val internFirstQuoteWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internFirstReceiptWallpaper = MutableStateFlow<Bitmap?>(null)


    fun getImageBitmapFromBadge(badge: Badge): ImageBitmap? {

        val file = File(badge.pathToImage)

        if (!file.exists())
            return null

        val imageBitmap = BitmapFactory
            .decodeFile(badge.pathToImage)
            .fixOrientation(badge.pathToImage)
            .asImageBitmap()

        return imageBitmap
    }

    fun fetchWallpaperFirstQuote() {
        viewModelScope.launch {
            repository.fetchWallpaper(BadgeIdentifier.FIRST_QUOTE_LIKED)

            val badge = internUserBadges.value.firstOrNull {
                it.identifier == BadgeIdentifier.FIRST_QUOTE_LIKED.ordinal
            }

            if (badge != null) {
                internFirstQuoteWallpaper.value = BitmapFactory
                    .decodeFile(badge.pathToImage)
                    .fixOrientation(badge.pathToImage)
            }
        }
    }

    fun fetchWallpaperFirstReceipt() {
        viewModelScope.launch {
            repository.fetchWallpaper(BadgeIdentifier.FIRST_RECEIPT)

            val badge = internUserBadges.value.firstOrNull {
                it.identifier == BadgeIdentifier.FIRST_RECEIPT.ordinal
            }

            if (badge != null) {
                internFirstReceiptWallpaper.value = BitmapFactory
                    .decodeFile(badge.pathToImage)
                    .fixOrientation(badge.pathToImage)
            }
        }
    }

    fun checkBadge(badgeIdentifier: BadgeIdentifier) {
        viewModelScope.launch {
            loadUserBadges()

            var badge = internUserBadges.value.firstOrNull() {
                it.identifier == badgeIdentifier.ordinal
            }

            if (badge == null) {
                badge = BadgeCatalog.getBadge(badgeIdentifier)
                badge.isGranted = true
                repository.insertUserBadge(badge)
                setBadgeAvailable()
                showToast(badge.title)
            } else {
                if (!badge.isGranted) {
                    repository.setBadgeGranted(badge.identifier, true)
                    setBadgeAvailable()
                    showToast(badge.title)
                }
            }
        }
    }

    fun loadUserBadges() {
        internUserBadges.value = repository.loadUserBadges()
    }

    fun setBadgeAvailable() {
        repository.setBadgeAvailable()
        internBadgeAvailable.value = true
    }

    fun resetBadgeAvailable() {
        repository.resetBadgeAvailable()
        internBadgeAvailable.value = false
    }
}