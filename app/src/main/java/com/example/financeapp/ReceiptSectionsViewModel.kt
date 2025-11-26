package com.example.financeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.forEach

class ReceiptSectionsViewModel(private val repository: ReceiptRepository): ViewModel() {

    private val internInsertState = MutableStateFlow<Boolean>(false)
    val insertState = internInsertState.asStateFlow()

    private val internReceipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts = internReceipts.asStateFlow()

    private val internReceiptsAverage = MutableStateFlow<Float>(0.0f)
    var receiptsAverage = internReceiptsAverage.asStateFlow()

    fun insertReceipt(receipt: Receipt) {

        val result = repository.insertReceipt(receipt)

        result.fold (
            onSuccess = { id ->
                internInsertState.value = true
                getReceipts()
                internInsertState.value = false
            },
            onFailure = {
                internInsertState.value = false
            }
        )
    }

    fun getReceipts() {

        val result = repository.getReceipts()

        result.fold (
            onSuccess = {
                internReceipts.value = it
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
}