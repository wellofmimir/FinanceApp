package com.example.financeapp.receiptsscreen

import androidx.lifecycle.ViewModel
import com.example.financeapp.database.Receipt
import com.example.financeapp.repositories.AdRepository
import com.example.financeapp.repositories.ReceiptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReceiptSectionsViewModel(private val repository: ReceiptRepository, private val adRepository: AdRepository): ViewModel() {

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

    private val internAccumulatedExpenses = MutableStateFlow<Float>(0.0f)
    val accumulatedExpenses = internAccumulatedExpenses.asStateFlow()

    private var currentTimespan = MutableStateFlow("" to "")

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
        }.sum().toFloat()

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

    fun addToInterstitialAdsSeen() {
        adRepository.addToInterstitialAdsSeen()
    }

    fun interstitialAdsSeen(): Int {
        return adRepository.interstitialAdsSeen()
    }
}