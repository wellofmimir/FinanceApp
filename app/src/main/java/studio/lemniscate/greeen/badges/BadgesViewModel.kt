package studio.lemniscate.greeen.badges

import studio.lemniscate.greeen.repositories.BadgesRepository
import studio.lemniscate.greeen.database.Badge
import studio.lemniscate.greeen.commonutils.fixOrientation

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
    private val internFourteenQuotesWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internFortyQuotesWallpaper = MutableStateFlow<Bitmap?>(null)


    private val internFirstReceiptWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internThirtyReceiptsWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internHundredReceiptsWallpaper = MutableStateFlow<Bitmap?>(null)

    private val internFirstGoalWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internTenGoalsWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internFiftyGoalsWallpaper = MutableStateFlow<Bitmap?>(null)

    private val internFirstDailyTipWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internThirtyDailyTipsWallpaper = MutableStateFlow<Bitmap?>(null)
    private val internNinetyDailyTipsWallpaper = MutableStateFlow<Bitmap?>(null)





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

    fun fetchWallpaper(badgeIdentifier: BadgeIdentifier) {
        viewModelScope.launch {
            repository.fetchWallpaper(badgeIdentifier)

            val badge = internUserBadges.value.firstOrNull {
                it.identifier == badgeIdentifier.ordinal
            }

            if (badge == null)
                return@launch

            repository.setBadgeAvailable()

            when (badgeIdentifier.ordinal) {
                BadgeIdentifier.FIRST_GOAL.ordinal -> {
                    internFirstGoalWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.TEN_GOALS.ordinal -> {
                    internTenGoalsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FIFTY_GOALS.ordinal -> {
                    internFiftyGoalsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FIRST_DAILY_TIP_LIKED.ordinal -> {
                    internFirstDailyTipWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.THIRTY_DAILY_TIPS_LIKED.ordinal -> {
                    internThirtyDailyTipsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.NINETY_DAILY_TIPS_LIKED.ordinal -> {
                    internNinetyDailyTipsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FIRST_QUOTE_LIKED.ordinal -> {
                    internFirstQuoteWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FOURTEEN_QUOTES_LIKED.ordinal -> {
                    internFourteenQuotesWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FORTY_QUOTES_LIKED.ordinal -> {
                    internFortyQuotesWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.FIRST_RECEIPT.ordinal -> {
                    internFirstReceiptWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.THIRTY_RECEIPTS.ordinal -> {
                    internThirtyReceiptsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                BadgeIdentifier.HUNDRED_RECEIPTS.ordinal -> {
                    internHundredReceiptsWallpaper.value = BitmapFactory
                        .decodeFile(badge.pathToImage)
                        .fixOrientation(badge.pathToImage)
                }

                else -> {
                    return@launch
                }
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