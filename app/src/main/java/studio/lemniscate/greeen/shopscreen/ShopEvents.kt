package studio.lemniscate.greeen.shopscreen

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ShopEvents {
    private val internNewThemePurchased = MutableSharedFlow<String> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val newThemePurchased = internNewThemePurchased.asSharedFlow()

    fun themeSuccessfullyPurchased(theme: String) {
        internNewThemePurchased.tryEmit(theme)
    }

    private val internAdRemoverPurchased = MutableSharedFlow<Boolean> (
        replay = 0,
        extraBufferCapacity = 1
    )

    val adRemoverPurchased = internAdRemoverPurchased.asSharedFlow()

    fun adRemoverSuccessfullyPurchased() {
        internAdRemoverPurchased.tryEmit(true)
    }
}