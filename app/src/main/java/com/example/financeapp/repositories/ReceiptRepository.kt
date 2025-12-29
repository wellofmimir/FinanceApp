package com.example.financeapp.repositories

import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.database.Receipt
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class ReceiptRepository private constructor (private val database: FinanceAppDatabase) {
    companion object {
        private var instance: ReceiptRepository? = null

        fun getInstance(database: FinanceAppDatabase): ReceiptRepository {
            if (instance == null)
                instance = ReceiptRepository(database)

            return instance!!
        }
    }

    fun insertReceipt(receipt: Receipt): Result<Long> {

        val currentDate = LocalDate.now()
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        return database.insertReceipt(receipt, formattedDate)
    }

    fun updateReceiptRemindMe(idReceipt: Int, date: String) {
        database.updateReceiptRemindMe(idReceipt, date)
    }

    fun getReceipts(): Result<List<Receipt>> {
        return database.getReceipts()
    }

    fun getReceiptsForACertainTimespan(startDate: String, endDate: String): Result<List<Receipt>> {
        return database.getReceipts(startDate, endDate)
    }

    fun getCurrentMonth(): String {

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)

        return formattedDate
    }

    fun getFirstDayOfCurrentMonth(): String {

        val today = LocalDate.now()
        val firstDayOfMonth = today.withDayOfMonth(1)

        val formatter = DateTimeFormatter.ISO_DATE
        return firstDayOfMonth.format(formatter)
    }

    fun getLastDayOfCurrentMonth(): String {

        val today = LocalDate.now()
        val lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth())

        val formatter = DateTimeFormatter.ISO_DATE
        return lastDayOfMonth.format(formatter)
    }

    fun getFirstDayOfTwoMonthsAgo(): String {

        val today = LocalDate.now().minusMonths(2)
        val firstDayOfTwoMonthsAgo = today.withDayOfMonth(1)

        val formatter = DateTimeFormatter.ISO_DATE
        return firstDayOfTwoMonthsAgo.format(formatter)
    }

    fun getFirstDayOfSixMonthsAgo(): String {

        val today = LocalDate.now().minusMonths(6)
        val firstDayOfSixMonthsAgo = today.withDayOfMonth(1)

        val formatter = DateTimeFormatter.ISO_DATE
        return firstDayOfSixMonthsAgo.format(formatter)
    }

    fun getFirstDayOfAYearAgo(): String {

        val today = LocalDate.now()
        val firstDayOfAYearAgo = today.minusYears(1)

        val formatter = DateTimeFormatter.ISO_DATE
        return firstDayOfAYearAgo.format(formatter)
    }

    fun getCurrency(): String {
        return database.getCurrency()
    }
}