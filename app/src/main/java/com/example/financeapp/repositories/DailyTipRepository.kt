package com.example.financeapp.repositories

import androidx.compose.runtime.collectAsState
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

    fun interstitialAdAfterDailyTipSeen(): Boolean {
        return database.interstitialAdAfterDailyTipSeen()
    }

    fun setInterstitialAdAfterDailyTipSeen() {
        database.setInterstitialAdAfterDailyTipSeen()
    }

    fun getDailyTip(): DailyTip {
        return database.getDailyTip()
    }

    fun newDailyTipAvailable(): Boolean {
        return database.newDailyTipAvailable()
    }

    fun setDailyTipAvailable() {
        database.setNewDailyTipAvailable()
    }

    fun resetDailyTipAvailable() {
        database.resetNewDailyTipAvailable()
    }

    fun fetchDailyTipFromServer() {

        //Ich will den Server nicht hardcore penetrieren,
        //da jede Abfrage mich bares Geld kostet.
        //Deswegen wird der Server nur einmal täglich abgefragt,
        //und das Ergebnis dann in den Shared Preferences gespeichert und davon bei Abfrage zurückgegeben.

        if (database.newDailyTipAvailable()) {
            return
        }

        val result = client.fetchDailyTip(object: DailyTipCallback {
            override fun result(response: String) {

                if (isValidJson(response) == false) {
                    return
                }

                if (!response.startsWith("{")) {
                    return
                }

                val jsonObject = JSONObject(response)
                val dailyTip = DailyTip(jsonObject.getString("title"), jsonObject.getString("fact"))

                setDailyTipAvailable()
                database.setDailyTip(dailyTip.title, dailyTip.tip)
            }
        })
    }

}