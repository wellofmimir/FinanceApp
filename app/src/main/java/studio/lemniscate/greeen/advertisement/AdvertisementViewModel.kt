package studio.lemniscate.greeen.advertisement

import androidx.lifecycle.ViewModel
import studio.lemniscate.greeen.repositories.AdRepository

class AdvertisementViewModel(private val repository: AdRepository): ViewModel() {

    fun setRemoveAllAds() {
        repository.setRemoveAllAds()
    }

    fun getRemoveAllAds(): Boolean {
        return true
        return repository.getRemoveAllAds()
    }

}