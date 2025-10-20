package com.example.financeapp

class QuoteRepository(private val database: FinanceAppDatabase) {

    fun getLikedQuotes(): List<Quote> {

        return listOf (
            Quote(1, "'Ficken'", "Patryk"),
            Quote(2, "'Fickenblasem'", "Mleczko")
        )
    }
}