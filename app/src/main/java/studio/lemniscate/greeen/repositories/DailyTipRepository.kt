package studio.lemniscate.greeen.repositories

import studio.lemniscate.greeen.database.Tip
import studio.lemniscate.greeen.commonutils.isValidJson
import studio.lemniscate.greeen.database.FinanceAppDatabase
import studio.lemniscate.greeen.network.DailyTip
import studio.lemniscate.greeen.network.DailyTipClient
import studio.lemniscate.greeen.commonutils.FileProvider
import studio.lemniscate.greeen.network.SharedHttpClient

import org.json.JSONObject


class DailyTipRepository private constructor (private val database: FinanceAppDatabase, private val fileProvider: FileProvider) {

    companion object {
        private var instance: DailyTipRepository? = null

        fun getInstance(database: FinanceAppDatabase, fileProvider: FileProvider): DailyTipRepository {
            if (instance == null)
                instance = DailyTipRepository(database, fileProvider)

            return instance!!
        }
    }

    private val client = DailyTipClient(SharedHttpClient.sharedClient)

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

        val result = client.fetchDailyTip()

        if (isValidJson(result) == false)
            return DailyTip("", "", "", "", "")

        if (!result.startsWith("{"))
            return DailyTip("", "", "", "", "")

        val imageToDailyTipResult = client.fetchImageToDailyTip()

        if (imageToDailyTipResult == null)
            return DailyTip("", "", "", "", "")

        val imageToDailyTipFile = fileProvider.getPNG()

        imageToDailyTipFile.outputStream().use {
            it.write(imageToDailyTipResult)
        }

        val jsonObject = JSONObject(result)

        val dailyTip = DailyTip (
            title = jsonObject.getString("title"),
            tip = jsonObject.getString("tip"),
            short = jsonObject.getString("short"),
            category = jsonObject.getString("category"),
            pathToImage = imageToDailyTipFile.absolutePath
        )

        if (dailyTip.tip != database.getDailyTip().tip) {
            database.setDailyTip(dailyTip.title, dailyTip.tip, dailyTip.short, dailyTip.category, dailyTip.pathToImage)
            database.setNewDailyTipAvailable()
        }

        return dailyTip
    }
}