package com.example.financeapp.advertisement

import androidx.lifecycle.ViewModel
import com.example.financeapp.repositories.AdRepository

class AdvertisementViewModel(private val repository: AdRepository): ViewModel() {

    fun setRemoveAllAds() {
        repository.setRemoveAllAds()
    }

    fun getRemoveAllAds(): Boolean {
        return repository.getRemoveAllAds()
    }

}