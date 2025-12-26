package com.example.financeapp.database

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import kotlin.getValue
import androidx.core.content.edit

import androidx.security.crypto.MasterKey
import androidx.security.crypto.EncryptedSharedPreferences

data class Goal (
    val id: Int,
    val goal: String,
    var amount: Float,
    var saved: Float,
    var idStatus: Int,
    val dateWhenFinished: String,
    val tokenCount: Int,
    val pathToImage: String
)

data class GoalStatus (
    val id: Int,
    val description: String
)

data class Quote (
    val id: Int,
    val quote: String,
    val name: String,
    val date: String
)

data class Receipt (
    val id: Int,
    val description: String,
    val amount: Float,
    val pathToImage: String,
    val date: String = "",
    val remindMeDate: String = ""
)

data class RemindMeEntry (
    val id: Int,
    val idReceipt: Int,
    val date: String
)

class FinanceAppDatabase private constructor(context: Context) {

    companion object {
        private var instance: FinanceAppDatabase? = null
        fun getInstance(context: Context): FinanceAppDatabase {
            if (instance == null) {
                instance = FinanceAppDatabase(context.applicationContext)
            }

            return instance!!
        }
    }


    private val database: SQLiteDatabase = context.openOrCreateDatabase("userdatabase.sqlite", Context.MODE_PRIVATE, null)
    private val databasePath = context.getDatabasePath("userdatabase.sqlite")

    init {
        //in SQLite sind Foreign-Keys standardmäßig ausgestellt, also müssen wir diese anschalten
        database.execSQL("PRAGMA foreign_keys = ON;")

        database.execSQL("CREATE TABLE IF NOT EXISTS userinformation (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, name TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS goalStatus (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, description TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS goals (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, goal TEXT NOT NULL, amount NUMERIC NOT NULL, saved NUMERIC NOT NULL, idStatus INTEGER NOT NULL, finishdate TEXT NOT NULL, tokencount INTEGER NOT NULL, pathtoimage TEXT NOT NULL, FOREIGN KEY (idStatus) REFERENCES goalStatus(id))".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS currentGoal (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, idGoal INTEGER NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, quote TEXT NOT NULL, name TEXT NOT NULL, date TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS currentQuote (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, quote TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS punchcard (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, tokensofar INTEGER NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS receipts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, description TEXT NOT NULL, amount NUMERIC NOT NULL, pathtoimage TEXT NOT NULL, date TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS receiptRemindDates (id INTEGER PRIMARY KEY AUTOINCREMENT, idReceipt INTERGER NOT NULL, date TEXT NOT NULL, FOREIGN KEY (idReceipt) REFERENCES receipts(id))".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS totalTokens (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, tokens INTEGER NOT NULL)".trimIndent())

        //Einfügen von Werten in currentGoal-Tabelle
        val values = ContentValues().apply {
            put("idGoal", 1)
        }

        database.insert("currentGoal", null, values)

        //einfügen von Werten in die goalStatus-Tabelle
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'InProgress' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'InProgress')")
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'Completed' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'Completed')")
    }

    //PREFERENCES - START

    //Hier wird der MasterKey erzeugt, der dann in den AndroidKeystore abgelegt wird

    private val masterKey by lazy {
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    }

    private val securePreferences by lazy {
        EncryptedSharedPreferences.create (
            context,
            "securePreferences",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val tutorialPreferences by lazy {
        context.getSharedPreferences("tutorialPreferences", Context.MODE_PRIVATE)
    }

    private val quotePreferences by lazy {
        context.getSharedPreferences("quotePreferences", Context.MODE_PRIVATE)
    }

    private val feedbackPreferences by lazy {
        context.getSharedPreferences("feedbackPreferences", Context.MODE_PRIVATE)
    }

    private val adPreferences by lazy {
        context.getSharedPreferences("adPreferences", Context.MODE_PRIVATE)
    }

    private val currencyPreferences by lazy {
        context.getSharedPreferences("currencyPreferences", Context.MODE_PRIVATE)
    }

    fun setThemePurchased(theme: String) {
        securePreferences.edit {
            putBoolean(theme, true)
        }
    }

    fun getThemePurchased(theme: String): Boolean {
        return securePreferences.getBoolean(theme, false)
    }

    fun getGoalHistoryTutorialDone(): Boolean {
        return tutorialPreferences.getBoolean("goalHistoryTutorialDone", false)
    }

    fun setGoalHistoryTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("goalHistoryTutorialDone", true)
        }
    }

    fun resetGoalHistoryTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("goalHistoryTutorialDone", false)
        }
    }

    fun setHomeScreenTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("homeScreenTutorialDone", true)
        }
    }

    fun getHomeScreenTutorialDone(): Boolean {
        return tutorialPreferences.getBoolean("homeScreenTutorialDone", false)
    }

    fun resetHomeScreenTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("homeScreenTutorialDone", false)
        }
    }

    fun getReceiptsTutorialDone(): Boolean {
        return tutorialPreferences.getBoolean("receiptsTutorialDone", false)
    }
    fun setReceiptsTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("receiptsTutorialDone", true)
        }
    }

    fun resetReceiptsTutorialDone() {
        tutorialPreferences.edit {
            putBoolean("receiptsTutorialDone", false)
        }
    }

    fun getCurrency(): String {
        return currencyPreferences.getString("currency", "$") ?: "$"
    }

    fun setCurrency(currency: String) {
        currencyPreferences.edit {
            putString("currency", currency)
        }
    }

    fun feedbackAlreadySent(): Boolean {
        return feedbackPreferences.getBoolean("feedbackSent", false)
    }

    fun setFeedbackSent() {
        feedbackPreferences.edit {
            putBoolean("feedbackSent", true)
        }
    }

    fun resetFeedbackSent() {
        feedbackPreferences.edit {
            putBoolean("feedbackSent", false)
        }
    }

    fun resetDailyQuoteFetched() {
        quotePreferences.edit {
            putBoolean("dailyQuoteFetched", false)
            putString("quote", "")
            putString("quotedPerson", "")
        }
    }

    fun saveDailyQuoteFetched(quote: Pair<String, String>) {
        quotePreferences.edit {
            putBoolean("dailyQuoteFetched", true)
            putString("quote", quote.first)
            putString("quotedPerson", quote.second)
        }
    }

    fun dailyQuoteFetched(): Boolean {
        return quotePreferences.getBoolean("dailyQuoteFetched", false)
    }

    fun dailyQuote(): Pair<String, String> {
        val quote = quotePreferences.getString("quote", "Time is money.") ?: "Time is money."
        val quotedPerson = quotePreferences.getString("quotedPerson", "Benjamin Franklin") ?: "Benjamin Franklin"
        return quote to quotedPerson
    }

    fun setInterstitialAdAfterReceiptSeen() {
        adPreferences.edit {
            putBoolean("interstitialAdAfterReceiptSeen", true)
        }
    }

    fun resetInterstitialAdAfterReceiptSeen() {
        adPreferences.edit {
            putBoolean("interstitialAdAfterReceiptSeen", false)
        }
    }

    fun interstitialAdAfterReceiptSeen(): Boolean {
        return adPreferences.getBoolean("interstitialAdAfterReceiptSeen", false)
    }

    //PREFERENCES - END

    fun updateReceiptRemindMe(idReceipt: Int, date: String) {

        val cursor = database.rawQuery("SELECT id FROM receiptRemindDates WHERE idReceipt = ?", arrayOf(idReceipt.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("idReceipt", idReceipt)
            put("date", date)
        }

        if (exists) {
            database.update("receiptRemindDates", values, "idReceipt = ?", arrayOf(idReceipt.toString()))
        } else {
            database.insert("receiptRemindDates", null, values)
        }
    }

    fun getReceiptRemindMe(): List<RemindMeEntry> {

        val cursor = database.rawQuery("SELECT * FROM receiptRemindDates", null)
        val receiptReminders = mutableListOf<RemindMeEntry>()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val idReceipt = cursor.getInt(cursor.getColumnIndexOrThrow("idReceipt"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

            val entry = RemindMeEntry(id, idReceipt, date)
            receiptReminders.add(entry)
        }

        cursor.close()
        return receiptReminders
    }

    fun addTotalTokensEarned(amount: Int) {

        val cursor = database.rawQuery("SELECT tokens FROM totalTokens WHERE id = ?", arrayOf("1"))
        val exists = cursor.moveToFirst()
        val current = if (exists) cursor.getInt(0) else 0
        cursor.close()

        val updatedValue = current + amount

        val values = ContentValues().apply {
            put("tokens", updatedValue)
        }

        if (exists) {
            database.update("totalTokens", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("totalTokens", null, values)
        }
    }
    fun getTotalTokensEarned(): Int {

        val cursor = database.rawQuery("SELECT * FROM totalTokens WHERE id = ?", arrayOf("1"))

        val totalTokensEarned = if (cursor.moveToFirst()) {
             cursor.getInt(cursor.getColumnIndexOrThrow("tokens"))
        } else {
            0
        }

        cursor.close()
        return totalTokensEarned
    }

    fun insertReceipt(receipt: Receipt, date: String): Result<Long>  {

        val values = ContentValues().apply {

            put("description", receipt.description)
            put("amount",      receipt.amount)
            put("pathtoimage", receipt.pathToImage)
            put("date",        date)
        }

        val id = database.insert("receipts", null, values)

        return if (id != -1L)
            Result.success(id)
        else
            Result.failure(Exception("Insert into receipts-table failed."))
    }

    fun getReceipts(startMonth: String, endMonth: String): Result<List<Receipt>>  {

        val cursor = database.query("receipts", arrayOf("id", "description", "amount", "pathtoimage", "date"), "date BETWEEN ? AND ?", arrayOf(startMonth, endMonth), null, null, "date DESC")
        val receipts = mutableListOf<Receipt>()

        try {
            while (cursor.moveToNext()) {

                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
                val pathToImage = cursor.getString(cursor.getColumnIndexOrThrow("pathtoimage"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                receipts.add(Receipt(id, description, amount, pathToImage, date))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Error in getReceipts()"))
        } finally {
            cursor.close()
        }

        return Result.success(receipts)
    }

    fun getReceipt(id: Int): Receipt {

        val cursor = database.rawQuery("SELECT * FROM receipts WHERE id = ?", arrayOf(id.toString()))
        val exists = cursor.moveToFirst()

        val receipt = if (exists) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
            val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
            val pathToImage = cursor.getString(cursor.getColumnIndexOrThrow("pathtoimage"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

            Receipt(id, description, amount, pathToImage, date)
        } else {
            Receipt(-1, "", 0.0f, "", "")
        }

        cursor.close()
        return receipt
    }

    fun getReceipts(): Result<List<Receipt>>  {

        val cursor = database.rawQuery("SELECT * FROM receipts ORDER BY date DESC", null)
        val receipts = mutableListOf<Receipt>()

        try {
            while (cursor.moveToNext()) {

                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
                val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
                val pathToImage = cursor.getString(cursor.getColumnIndexOrThrow("pathtoimage"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

                receipts.add(Receipt(id, description, amount, pathToImage, date))
            }
        } catch (e: Exception) {
            return Result.failure(Exception("Error in getReceipts()"))
        } finally {
            cursor.close()
        }

        return Result.success(receipts)
    }

    fun updateUser(name: String) {

        val cursor = database.rawQuery("SELECT id FROM userinformation WHERE id = 1", null)
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("name", name)
        }

        if (exists) {
            database.update("userinformation", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("userinformation", null, values)
        }
    }

    fun getCurrentQuote(): String {

        val cursor = database.rawQuery("SELECT id FROM currentQuote WHERE id = 1", null)

        val currentQuote = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow("quote"))
        } else {
            ""
        }

        cursor.close()
        return currentQuote
    }

    fun updateCurrentQuote(quote: String) {

        val cursor = database.rawQuery("SELECT id FROM currentQuote WHERE id = 1", null)
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("quote", quote)
        }

        if (exists) {
            database.update("currentQuote", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("currentQuote", null, values)
        }
    }

    fun getAllQuotes(): List<Quote> {

        val cursor = database.rawQuery("SELECT * FROM quotes", null)
        val quotes = mutableListOf<Quote>()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val quote = cursor.getString(cursor.getColumnIndexOrThrow("quote"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))

            quotes.add(Quote(id, quote, name, date))
        }

        cursor.close()
        return quotes
    }

    fun insertQuote(quote: String, name: String, formattedDate: String) {

        val cursor = database.rawQuery("SELECT * FROM quotes WHERE quote = ?", arrayOf(quote))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("quote", quote)
        values.put("name", name)
        values.put("date", formattedDate)

        if (exists) {
            return
        } else {
            database.insert("quotes", null, values)
        }
    }

    fun deleteQuote(quote: String) {

        val cursor = database.rawQuery("SELECT * FROM quotes WHERE quote = ?", arrayOf(quote))
        val exists = cursor.moveToFirst()
        cursor.close()

        if (exists) {
            database.delete("quotes", "quote = ?", arrayOf(quote))
        }
    }

    fun getUser(): String {

        val cursor = database.rawQuery("SELECT name FROM userinformation WHERE id = 1", null)

        val user = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow("name"))
        } else {
            "DUMMY"
        }

        cursor.close()
        return user
    }
    fun getIDGoalStatus(description: String): GoalStatus? {

        val cursor = database.rawQuery("SELECT id, description FROM goalStatus WHERE description = ?", arrayOf(description))

        cursor.use {

            if (it.moveToFirst()) {

                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val descriptionFromQuery = it.getString(it.getColumnIndexOrThrow("description"))
                return GoalStatus(id, descriptionFromQuery)
            }
        }

        return null
    }

    fun setCurrentGoal(goal: Goal) {

        val cursor = database.rawQuery("SELECT id FROM currentGoal WHERE id = 1", null)
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {put("idGoal", goal.id)}

        if (exists) {
            database.update("currentGoal", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("currentGoal", null, values)
        }
    }

    fun currentGoal(): Goal? {

        val cursor = database.rawQuery("SELECT * FROM currentGoal WHERE id = ?", arrayOf("1"))

        cursor.use {

            if (it.moveToFirst()) {
                val goalId = it.getInt(it.getColumnIndexOrThrow("idGoal"))
                return getGoal(goalId)
            }
        }

        return null
    }

    fun updateGoal(goal: Goal) {

        val cursor = database.rawQuery("SELECT id FROM goals WHERE id = ?", arrayOf(goal.id.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("id",          goal.id)
            put("goal",        goal.goal)
            put("amount",      goal.amount)
            put("saved",       goal.saved)
            put("idStatus",    goal.idStatus)
            put("finishdate",  goal.dateWhenFinished)
            put("tokencount",  goal.tokenCount)
            put("pathtoimage", goal.pathToImage)
        }

        if (exists) {
            database.update("goals", values, "id = ?", arrayOf(goal.id.toString()))
        }
    }

    fun updateImageToGoal(idGoal: Int, pathToImage: String) {

        val cursor = database.rawQuery("SELECT id FROM goals WHERE id = ?", arrayOf(idGoal.toString()))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("id",          idGoal)
            put("pathtoimage", pathToImage)
        }

        if (exists) {
            database.update("goals", values, "id = ?", arrayOf(idGoal.toString()))
        }
    }

    fun getGoal(id: Int): Goal? {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE id = ?", arrayOf(id.toString()))

        cursor.use {

            if (it.moveToFirst()) {

                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val goal = it.getString(it.getColumnIndexOrThrow("goal"))
                val amount = it.getFloat(it.getColumnIndexOrThrow("amount"))
                val savedAmount = it.getFloat(it.getColumnIndexOrThrow("saved"))
                val idStatus = it.getInt(it.getColumnIndexOrThrow("idStatus"))
                val dateWhenFinished = it.getString(it.getColumnIndexOrThrow("finishdate"))
                val tokenCount = it.getInt(it.getColumnIndexOrThrow("tokencount"))
                val pathToImage = it.getString(it.getColumnIndexOrThrow("pathtoimage"))

                return Goal(id, goal, amount, savedAmount, idStatus, dateWhenFinished, tokenCount, pathToImage)
            }
        }

        return null
    }

    fun resetPunchcard() {

        val cursor = database.rawQuery("SELECT tokensofar FROM punchcard WHERE id = ?", arrayOf("1"))
        val exists = cursor.moveToFirst()
        val current = if (exists) cursor.getInt(0) else 0
        cursor.close()

        val values = ContentValues().apply {
            put("tokensofar", 0)
        }

        if (exists) {
            database.update("punchcard", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("punchcard", null, values)
        }
    }

    fun setTokenSoFarForPunchcard(tokenSoFar: Int) {

        val cursor = database.rawQuery("SELECT tokensofar FROM punchcard WHERE id = ?", arrayOf("1"))
        val exists = cursor.moveToFirst()
        val current = if (exists) cursor.getInt(0) else 0
        cursor.close()

        val updatedValue = current + tokenSoFar

        val values = ContentValues().apply {
            put("tokensofar", updatedValue)
        }

        if (exists) {
            database.update("punchcard", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("punchcard", null, values)
        }
    }
    fun getTokenSoFarForPunchcard(): Int {

        val cursor = database.rawQuery("SELECT * FROM punchcard WHERE id = 1", null)

        cursor.use {
            if (it.moveToFirst()) {
                val tokenSoFar = it.getInt(it.getColumnIndexOrThrow("tokensofar"))
                return tokenSoFar
            }
        }

        return -1
    }

    fun getGoals(): List<Goal> {

        val cursor = database.rawQuery("SELECT * FROM goals", null)
        val goals = mutableListOf<Goal>()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val goal = cursor.getString(cursor.getColumnIndexOrThrow("goal"))
            val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
            val savedAmount = cursor.getFloat(cursor.getColumnIndexOrThrow("saved"))
            val idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus"))
            val dateWhenFinished = cursor.getString(cursor.getColumnIndexOrThrow("finishdate"))
            val tokenCount = cursor.getInt(cursor.getColumnIndexOrThrow("tokencount"))
            val pathToImage = cursor.getString(cursor.getColumnIndexOrThrow("pathtoimage"))

            goals.add(Goal (id, goal, amount, savedAmount, idStatus, dateWhenFinished, tokenCount, pathToImage))
        }

        cursor.close()
        return goals
    }

    fun insertGoal(nameOfGoal: String, amount: Float, savedAmount: Float, goalStatus: String, tokenCount: Int = 1) {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE goal = ?", arrayOf(nameOfGoal))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("goal", nameOfGoal)
        values.put("amount", amount)
        values.put("saved", savedAmount)
        values.put("idStatus", getIDGoalStatus(goalStatus)!!.id)
        values.put("finishdate", "")
        values.put("tokencount", tokenCount)
        values.put("pathtoimage", "")

        if (exists) {
            database.update("goals", values, "goal = ?", arrayOf(nameOfGoal))
        } else {
            database.insert("goals", null, values)
        }
    }

    fun insertGoal(goal: Goal) {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE goal = ?", arrayOf(goal.goal))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("goal",        goal.goal)
        values.put("amount",      goal.amount)
        values.put("saved",       goal.saved)
        values.put("idStatus",    goal.idStatus)
        values.put("finishdate",  goal.dateWhenFinished)
        values.put("tokencount",  goal.tokenCount)
        values.put("pathtoimage", goal.pathToImage)

        if (exists) {
            database.update("goals", values, "goal = ?", arrayOf(goal.goal))
        } else {
            database.insert("goals", null, values)
        }
    }

    fun getNewestGoalId(): Int {

        val cursor = database.rawQuery("SELECT id FROM goals ORDER BY id DESC LIMIT 1", null)
        var newestId = -1

        if (cursor.moveToFirst()) {
            if (!cursor.isNull(0)) {
                newestId = cursor.getInt(0)
            }
        }

        cursor.close()
        return newestId
    }
    
    fun deleteDatabase() {

        if (databasePath.exists()) {
            databasePath.delete()
        }
    }
}