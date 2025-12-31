package com.example.financeapp.repositories

import com.example.financeapp.database.Tip
import com.example.financeapp.commonutils.isValidJson
import com.example.financeapp.database.FinanceAppDatabase
import com.example.financeapp.network.DailyTip
import com.example.financeapp.network.DailyTipCallback
import com.example.financeapp.network.DailyTipClient
import org.json.JSONObject

class DailyTipRepository private constructor (private val database: FinanceAppDatabase) {
    companion object {
        private var instance: DailyTipRepository? = null

        fun getInstance(database: FinanceAppDatabase): DailyTipRepository {
            if (instance == null)
                instance = DailyTipRepository(database)

            return instance!!
        }
    }

    private val client = DailyTipClient.getInstance()

    fun resetNewDailyTipAvailable() {
        database.resetNewDailyTipAvailable()
    }
    fun newDailyTipAvailable(): Boolean {
        return database.newDailyTipAvailable()
    }

    fun insertDailyTip(dailyTip: DailyTip) {
        database.insertTip(dailyTip)
    }

    fun getLikedTips(): List<Tip> {
        return database.getTips()
    }

    fun getLikedTipsRandomlyOrdered(): List<Tip> {
        return database.getTipsRandomlyOrdered()
    }

    fun removeDailyTip(dailyTip: DailyTip) {
        database.removeDailyTip(dailyTip)
    }

    fun resetDailyTip() {
        database.setDailyTip("", "")
    }

    fun interstitialAdAfterDailyTipSeen(): Boolean {
        return database.interstitialAdAfterDailyTipSeen()
    }

    fun setInterstitialAdAfterDailyTipSeen() {
        database.setInterstitialAdAfterDailyTipSeen()
    }

    fun getDailyTip(): DailyTip {
        return database.getDailyTip()
    }

    suspend fun fetchDailyTipFromServer(): DailyTip {

        //Ich will den Server nicht hardcore penetrieren,
        //da jede Abfrage mich bares Geld kostet.
        //Deswegen wird der Server nur einmal täglich abgefragt,
        //und das Ergebnis dann in den Shared Preferences gespeichert und davon bei Abfrage zurückgegeben.

        if (database.getDailyTip().tip.isNotEmpty()) {
            return DailyTip("", "")
        }

        val result = client.fetchDailyTip()

        if (isValidJson(result) == false)
            return DailyTip("", "")

        if (!result.startsWith("{"))
            return DailyTip("", "")

        val jsonObject = JSONObject(result)
        val dailyTip = DailyTip(jsonObject.getString("title"), jsonObject.getString("fact"))

        if (dailyTip.tip != database.getDailyTip().tip) {
            database.setDailyTip(dailyTip.title, dailyTip.tip)
            database.setNewDailyTipAvailable()
        }

        return dailyTip
    }
}