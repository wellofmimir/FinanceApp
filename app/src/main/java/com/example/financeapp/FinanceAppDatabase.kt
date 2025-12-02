package com.example.financeapp

import android.R
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import kotlin.getValue
import androidx.core.content.edit

data class Goal (

    val id: Int,
    val goal: String,
    var amount: Float,
    var saved: Float,
    var idStatus: Int,
    val dateWhenFinished: String,
    val tokenCount: Int
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

        database.execSQL("CREATE TABLE IF NOT EXISTS userinformation (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, name TEXT NOT NULL, tutorialdone INTEGER NOT NULL DEFAULT 0)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS goalStatus (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, description TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS goals (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, goal TEXT NOT NULL, amount NUMERIC NOT NULL, saved NUMERIC NOT NULL, idStatus INTEGER NOT NULL, finishdate TEXT NOT NULL, tokencount INTEGER NOT NULL, FOREIGN KEY (idStatus) REFERENCES goalStatus(id))".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS currentGoal (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, idGoal INTEGER NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, quote TEXT NOT NULL, name TEXT NOT NULL, date TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS currentQuote (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, quote TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS punchcard (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, tokensofar INTEGER NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS receipts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, description TEXT NOT NULL, amount NUMERIC NOT NULL, pathtoimage TEXT NOT NULL, date TEXT NOT NULL)".trimIndent())

        //Einfügen von Werten in currentGoal-Tabelle
        val values = ContentValues().apply {
            put("idGoal", 1)
        }

        database.insert("currentGoal", null, values)

        //einfügen von Werten in die goalStatus-Tabelle
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'InProgress' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'InProgress')")
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'Completed' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'Completed')")
    }

    private val quotePreferences by lazy {
        context.getSharedPreferences("quotePreferences", Context.MODE_PRIVATE)
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

    fun isTutorialDone(): Boolean {

        val cursor = database.rawQuery("SELECT tutorialdone FROM userinformation WHERE id = 1", null)

        val tutorialDone: Int = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("tutorialdone"))
        } else {
            0
        }

        cursor.close()
        return tutorialDone == 1
    }

    fun updateTutorialStatus(status: Int) {

        val cursor = database.rawQuery("SELECT * FROM userinformation WHERE id = 1", null)
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {
            put("tutorialdone", status)
        }

        if (exists) {
            database.update("userinformation", values, "id = ?", arrayOf("1"))
        } else {
            values.put("tutorialdone", status)
            database.insert("userinformation", null, values)
        }
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
            put("id",         goal.id)
            put("goal",       goal.goal)
            put("amount",     goal.amount)
            put("saved",      goal.saved)
            put("idStatus",   goal.idStatus)
            put("finishdate", goal.dateWhenFinished)
            put("tokencount", goal.tokenCount)
        }

        if (exists) {
            database.update("goals", values, "id = ?", arrayOf(goal.id.toString()))
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

                return Goal(id, goal, amount, savedAmount, idStatus, dateWhenFinished, tokenCount)
            }
        }

        return null
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

            goals.add(Goal (id, goal, amount, savedAmount, idStatus, dateWhenFinished, tokenCount))
        }

        cursor.close()
        return goals
    }

    fun insertGoal(nameOfGoal: String, amount: Float, savedAmount: Float, goalStatus: String) {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE goal = ?", arrayOf(nameOfGoal))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("goal", nameOfGoal)
        values.put("amount", amount)
        values.put("saved", savedAmount)
        values.put("idStatus", getIDGoalStatus(goalStatus)!!.id)
        TODO("finishdate einbauen")

        if (exists) {
             return
        } else {
            database.insert("goals", null, values)
        }
    }

    fun insertGoal(goal: Goal) {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE goal = ?", arrayOf(goal.goal))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("goal", goal.goal)
        values.put("amount", goal.amount)
        values.put("saved", goal.saved)
        values.put("idStatus", goal.idStatus)
        values.put("finishdate", goal.dateWhenFinished)
        values.put("tokencount", goal.tokenCount)

        if (exists) {
            return
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