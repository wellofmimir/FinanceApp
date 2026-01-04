package com.example.financeapp.receiptsscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Receipt
import com.example.financeapp.repositories.AdRepository
import com.example.financeapp.repositories.ReceiptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

sealed interface ShareEvent {
    data class SharedReceipt (val imageUri: Uri, val text: String): ShareEvent
}

class ReceiptSectionsViewModel(private val repository: ReceiptRepository, private val adRepository: AdRepository): ViewModel() {

    private val internShareEvent = MutableSharedFlow<ShareEvent>()
    val shareEvent = internShareEvent.asSharedFlow()

    fun shareReceipt(receipt: Receipt) {
        viewModelScope.launch {
            getCurrency()
            internShareEvent.emit(ShareEvent.SharedReceipt(imageUri = receipt.pathToImage.toUri(), text = "Checkout the Greeen-App to track your receipts easy and 100% free.\n\n${receipt.description}\n${currency.value + " " + receipt.amount}"))
        }
    }

    private val internInsertState = MutableStateFlow<Boolean>(false)
    val insertState = internInsertState.asStateFlow()
    private val internReceipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts = internReceipts.asStateFlow()
    private val internReceiptsAverage = MutableStateFlow<Float>(0.0f)
    val receiptsAverage = internReceiptsAverage.asStateFlow()
    private val internReceiptsSum = MutableStateFlow<Float>(0.0f)
    val receiptsSum = internReceiptsSum.asStateFlow()
    private val internCurrentMonth = MutableStateFlow<String>("")
    val currentMonth = internCurrentMonth.asStateFlow()
    private var currentTimespan = MutableStateFlow("" to "")
    private var internCurrency = MutableStateFlow("")
    var currency = internCurrency.asStateFlow()

    fun insertReceipt(receipt: Receipt, remindMeDate: String = "") {

        val result = repository.insertReceipt(receipt)

        result.fold (
            onSuccess = { id ->
                internInsertState.value = true
                getReceiptsForACertainTimespan(currentTimespan.value.first, currentTimespan.value.second)
                updateReceiptRemindMe(id.toInt(), remindMeDate)
                internInsertState.value = false
            },
            onFailure = {
                internInsertState.value = false
            }
        )
    }

    fun deleteReceipt(receipt: Receipt) {
        repository.deleteReceipt(receipt)
        getReceipts()
    }

    fun updateReceiptRemindMe(idReceipt: Int, date: String) {
        repository.updateReceiptRemindMe(idReceipt, date)
    }

    fun getReceipts() {
        val result = repository.getReceipts()

        result.fold (
            onSuccess = {
                internReceipts.value = it
                calculateSum()
            },
            onFailure = {
                internReceipts.value = emptyList()
            }
        )
    }

    fun getReceiptsForACertainTimespan(startDate: String, endDate: String) {

        val result = repository.getReceiptsForACertainTimespan(startDate, endDate)
        currentTimespan.value = startDate to endDate

        result.fold (
            onSuccess = {
                internReceipts.value = it
                calculateSum()
            },
            onFailure = {
                internReceipts.value = emptyList()
            }
        )
    }

    fun calculateAverage() {

        val receipts = internReceipts.value

        if (receipts.isEmpty()) {
            internReceiptsAverage.value = 0f
            return
        }

        val average = receipts.map { receipt ->
            receipt.amount
        }.average().toFloat()

        internReceiptsAverage.value = average
    }

    fun calculateSum() {

        val receipts = internReceipts.value

        if (receipts.isEmpty()) {
            internReceiptsSum.value = 0f
            return
        }

        val average = receipts.map { receipt ->
            receipt.amount
        }.sum()

        internReceiptsSum.value = average
    }

    fun getCurrentMonth() {
        internCurrentMonth.value = repository.getCurrentMonth()
    }

    fun getFirstDayOfCurrentMonth(): String {
        return repository.getFirstDayOfCurrentMonth()
    }

    fun getLastDayOfCurrentMonth(): String {
        return repository.getLastDayOfCurrentMonth()
    }

    fun getFirstDayOfSixMonthsAgo(): String {
        return repository.getFirstDayOfSixMonthsAgo()
    }

    fun getFirstDayOfTwoMonthsAgo(): String {
        return repository.getFirstDayOfTwoMonthsAgo()
    }
    fun getFirstDayOfAYearAgo(): String {
        return repository.getFirstDayOfAYearAgo()
    }

    fun setInterstitialAdAfterReceiptSeen() {
        adRepository.setInterstitialAdAfterReceiptSeen()
    }

    fun interstitialAdAfterReceiptSeen(): Boolean {
        return adRepository.interstitialAdAfterReceiptSeen()
    }
    fun getCurrency() {
        internCurrency.value = repository.getCurrency()
    }
}