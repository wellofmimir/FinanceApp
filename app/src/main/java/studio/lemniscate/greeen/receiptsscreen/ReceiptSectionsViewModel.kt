package studio.lemniscate.greeen.receiptsscreen

import android.net.Uri
import androidx.core.net.toUri

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import studio.lemniscate.greeen.database.Receipt
import studio.lemniscate.greeen.repositories.AdRepository
import studio.lemniscate.greeen.repositories.ReceiptRepository
import studio.lemniscate.greeen.database.Expense
import studio.lemniscate.greeen.network.TrendRequest

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch



sealed interface ShareEvent {
    data class SharedReceipt (val imageUri: Uri, val text: String): ShareEvent
}

class ReceiptSectionsViewModel (
    private val repository: ReceiptRepository,
    private val adRepository: AdRepository
): ViewModel() {
    private val internShareForWhatsAppEvent = MutableSharedFlow<ShareEvent>()
    val shareEventForWhatsApp = internShareForWhatsAppEvent.asSharedFlow()

    private val internShareForFacebookMessengerEvent = MutableSharedFlow<ShareEvent>()
    val shareEventForFacebookMessenger = internShareForFacebookMessengerEvent.asSharedFlow()

    private val internShareForFacebookEvent = MutableSharedFlow<ShareEvent>()
    val shareEventForFacebook = internShareForFacebookEvent.asSharedFlow()

    fun shareReceiptOnWhatsApp(receipt: Receipt) {
        viewModelScope.launch {
            getCurrency()
            internShareForWhatsAppEvent.emit(ShareEvent.SharedReceipt(imageUri = receipt.pathToImage.toUri(), text = "${receipt.description}\n${currency.value + " " + receipt.amount}\n\nCheckout the Greeen-App to track your receipts easy and 100% free."))
        }
    }

    fun shareReceiptOnFacebookMessenger(receipt: Receipt) {
        viewModelScope.launch {
            getCurrency()
            internShareForFacebookMessengerEvent.emit(ShareEvent.SharedReceipt(imageUri = receipt.pathToImage.toUri(), text = "${receipt.description}\n${currency.value + " " + receipt.amount}\n\nCheckout the Greeen-App to track your receipts easy and 100% free."))
        }
    }

    fun shareReceiptOnFacebook(receipt: Receipt) {
        viewModelScope.launch {
            getCurrency()
            internShareForFacebookEvent.emit(ShareEvent.SharedReceipt(imageUri = receipt.pathToImage.toUri(), text = "${receipt.description}\n${currency.value + " " + receipt.amount}\n\nCheckout the Greeen-App to track your receipts easy and 100% free."))
        }
    }

    private val internExpenses = MutableStateFlow(listOf (
            Expense("Housing", "Hou.", 0f),
            Expense("Food", "Food",0f),
            Expense("Transportation", "Trans.",0f),
            Expense("Utilities", "Util.",0f),
            Expense("Subscriptions", "Subs.",0f),
            Expense("Health", "Hlth.",0f),
            Expense("Shopping", "Shop.", 0f),
            Expense("Entertainment", "Entr.",0f),
            Expense("Travel", "Trvl.",0f),
            Expense("Education", "Edu.", 0f),
            Expense("Personal", "Prs.",0f),
            Expense("Family", "Fam.", 0f),
            Expense("Saving", "Sav.", 0f),
            Expense("Other", "Oth.", 0f)
        )
    )

    val expenses = internExpenses.asStateFlow()

    private val internExpense = MutableStateFlow(Expense("", "", 0f))
    val expense = internExpense.asStateFlow()

    private val internInsertState = MutableStateFlow(false)
    val insertState = internInsertState.asStateFlow()
    private val internReceipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts = internReceipts.asStateFlow()
    private val internReceiptsAverage = MutableStateFlow(0.0f)
    val receiptsAverage = internReceiptsAverage.asStateFlow()
    private val internReceiptsSum = MutableStateFlow(0.0f)
    val receiptsSum = internReceiptsSum.asStateFlow()
    private val internCurrentMonth = MutableStateFlow("")
    val currentMonth = internCurrentMonth.asStateFlow()
    private var currentTimespan = MutableStateFlow("" to "")
    private var internCurrency = MutableStateFlow("")
    var currency = internCurrency.asStateFlow()
    private val internToastEvent = MutableSharedFlow<String>()
    val toastEvent = internToastEvent.asSharedFlow()

    private var internTrendRequest = MutableStateFlow(TrendRequest("", "", emptyList()))
    val trendRequest = internTrendRequest.asStateFlow()

    fun getRandomSeriesOfValuesForTrendAnalysis() {
        val randomEntry = internExpenses.value.random()
        val randomNumber = (1..5).random()

        val result = when (randomNumber) {
            1 -> repository.getReceiptsForACertainTimespanAndCategory(getFirstDayOfCurrentMonth(), getLastDayOfCurrentMonth(), randomEntry.category)
            else -> repository.getReceiptsForACertainTimespanAndCategory(getFirstDayOfCurrentMonth(), getLastDayOfCurrentMonth(), randomEntry.category)
        }

        val timeUnit = when (randomNumber) {
            1 -> "days"
            2 -> "months"
            3 -> "years"
            else -> ""
        }

        val valuesList: List<Float> = result.map { receipt ->
            receipt.amount
        }

        internTrendRequest.value = TrendRequest(randomEntry.category, timeUnit, valuesList)
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            internToastEvent.emit(message)
        }
    }

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
        showToast("Receipt deleted.")
        getReceipts()
    }

    fun updateReceiptRemindMe(idReceipt: Int, date: String) {
        repository.updateReceiptRemindMe(idReceipt, date)
    }

    fun getReceipts() {
        resetReceipts()
        val result = repository.getReceipts()

        result.fold (
            onSuccess = {
                internReceipts.value = it
                calculateSum()
                calculateExpenses()
            },
            onFailure = {
                internReceipts.value = emptyList()
            }
        )
    }

    fun getReceiptsForACertainTimespan (
        startDate: String,
        endDate: String
    ) {
        resetReceipts()
        val result = repository.getReceiptsForACertainTimespan(startDate, endDate)
        currentTimespan.value = startDate to endDate

        result.fold (
            onSuccess = {
                internReceipts.value = it
                calculateSum()
                calculateExpenses()
            },
            onFailure = {
                internReceipts.value = emptyList()
            }
        )
    }

    private fun resetReceipts() {
        internReceipts.value = emptyList()
    }

    fun getExpense (
        category: String
    ) {
        internExpense.value =  internExpenses.value.firstOrNull { expense ->
            expense.category == category
        } ?: Expense("", "", 0f)
    }

    fun calculateExpenses() {
        val newExpenses = internExpenses.value.map { expense ->
            val sum = internReceipts.value
                .filter { receipt ->
                    receipt.category == expense.category
                }
                .sumOf { receipt ->
                    receipt.amount.toDouble()
                }
                .toFloat()

            expense.copy(amount = sum)
        }

        internExpenses.value = newExpenses.sortedByDescending { it.amount }
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

    private fun calculateSum() {
        val receipts = internReceipts.value

        if (receipts.isEmpty()) {
            internReceiptsSum.value = 0f
            return
        }

        val sum = receipts.map { receipt ->
            receipt.amount
        }.sum()

        internReceiptsSum.value = sum
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

    private val internShowAddReceiptSection = MutableStateFlow(false)
    val showAddReceiptSection = internShowAddReceiptSection.asStateFlow()

    fun showAddReceiptSection() {
        internShowAddReceiptSection.value = true
    }

    fun closeAddReceiptSection() {
        internShowAddReceiptSection.value = false
    }
}