package com.example.financeapp.badges

import com.example.financeapp.repositories.BadgesRepository
import com.example.financeapp.database.Badge

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.commonutils.fixOrientation
import com.example.financeapp.network.DailyTip
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BadgesViewModel (
    private val repository: BadgesRepository,
): ViewModel() {
    private val internUserBadges = MutableStateFlow<List<Badge>>(emptyList())
    val userBadges = internUserBadges.asStateFlow()

    private val internToastForFirstQuote = MutableStateFlow<Pair<String, String>>("" to "")
    val toastForFirstQuote = internToastForFirstQuote.asStateFlow()

    private val internFirstQuoteWallpaper = MutableStateFlow<Bitmap?>(null)
    val firstQuoteWallpaper: StateFlow<ImageBitmap?> = internFirstQuoteWallpaper
        .map {
            it?.asImageBitmap()
        }
        .stateIn (
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun getImageBitmapFromBadge(badge: Badge): ImageBitmap {
        val imageBitmap = BitmapFactory
            .decodeFile(badge.pathToImage)
            .fixOrientation(badge.pathToImage)
            .asImageBitmap()

        return imageBitmap
    }

    fun fetchWallpaperFirstQuote() {
        viewModelScope.launch {
            repository.fetchWallpaperFirstQuote()

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

    fun checkFirstQuoteBadge() {
        viewModelScope.launch {
            loadUserBadges()

            var badge = internUserBadges.value.firstOrNull {
                it.identifier == BadgeIdentifier.FIRST_QUOTE_LIKED.ordinal
            }

            if (badge == null) {
                badge = BadgeCatalog.getBadge(BadgeIdentifier.FIRST_QUOTE_LIKED)
                badge.isGranted = true
                repository.insertUserBadge(badge)
                internToastForFirstQuote.value = Pair(badge.title, badge.text)
            } else {
                if (!badge.isGranted) {
                    repository.setBadgeGranted(badge.identifier, true)
                    internToastForFirstQuote.value = Pair(badge.title, badge.text)
                }
            }
        }
    }

    fun loadUserBadges() {
        internUserBadges.value = repository.loadUserBadges()
    }
}