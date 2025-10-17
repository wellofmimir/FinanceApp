package com.example.financeapp

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import androidx.compose.runtime.mutableStateOf

data class Goal (

    val id: Int,
    val goal: String,
    val amount: Float,
    val idStatus: Int
)

data class GoalStatus (

    val id: Int,
    val description: String
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
        database.execSQL("CREATE TABLE IF NOT EXISTS goals (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, goal TEXT NOT NULL, amount NUMERIC NOT NULL, idStatus INTEGER NOT NULL, FOREIGN KEY (idStatus) REFERENCES goalStatus(id))".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS goalStatus (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, description TEXT NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS currentGoal (id INTEGER PRIMARY KEY CHECK (id = 1) NOT NULL, idGoal INTEGER NOT NULL)".trimIndent())
        database.execSQL("CREATE TABLE IF NOT EXISTS quotes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, quote TEXT NOT NULL, name TEXT NOT NULL)".trimIndent())

        //Einfügen von Werten in currentGoal-Tabelle
        val values = ContentValues().apply {
            put("idGoal", 1)
        }
        database.insert("currentGoal", null, values)

        //einfügen von Werten in die goalStatus-Tabelle
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'InProgress' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'InProgress')")
        database.execSQL("INSERT INTO goalStatus (description) SELECT 'Completed' WHERE NOT EXISTS (SELECT 1 FROM goalStatus WHERE description = 'Completed')")
    }

    fun updateUser(name: String) {

        val cursor = database.rawQuery("SELECT id FROM userinformation WHERE id = 1", null)
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues().apply {put("name", name)}

        if (exists) {
            database.update("userinformation", values, "id = ?", arrayOf("1"))
        } else {
            values.put("id", 1)
            database.insert("userinformation", null, values)
        }
    }

    fun insertQuote(quote: String, name: String) {

        val cursor = database.rawQuery("SELECT * FROM quotes WHERE quote = ?", arrayOf(quote))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("quote", quote)
        values.put("name", name)

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
            cursor.getString(0)
        } else {
            "DUMMY"
        }

        cursor.close()
        return user
    }

    fun getIDGoalStatus(description: String): GoalStatus? {

        val cursor = database.rawQuery("SELECT id, description FROM goalStatus WHERE description = ?", arrayOf(description))
        var status: GoalStatus? = null

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

    fun getGoal(id: Int): Goal? {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE id = ?", arrayOf(id.toString()))

        cursor.use {

            if (it.moveToFirst()) {

                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val goal = it.getString(it.getColumnIndexOrThrow("goal"))
                val amount = it.getFloat(it.getColumnIndexOrThrow("amount"))
                val idStatus = it.getInt(it.getColumnIndexOrThrow("idStatus"))

                return Goal(id, goal, amount, idStatus)
            }
        }

        return null
    }

    fun getGoals(): List<Goal> {

        val cursor = database.rawQuery("SELECT * FROM goals", null)
        val goals = mutableListOf<Goal>()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val goal = cursor.getString(cursor.getColumnIndexOrThrow("goal"))
            val amount = cursor.getFloat(cursor.getColumnIndexOrThrow("amount"))
            val idStatus = cursor.getInt(cursor.getColumnIndexOrThrow("idStatus"))

            goals.add(Goal (id, goal, amount, idStatus))
        }

        cursor.close()
        return goals
    }

    fun insertGoal(nameOfGoal: String, amount: Float, goalStatus: String) {

        val cursor = database.rawQuery("SELECT * FROM goals WHERE goal = ?", arrayOf(nameOfGoal))
        val exists = cursor.moveToFirst()
        cursor.close()

        val values = ContentValues()
        values.put("goal", nameOfGoal)
        values.put("amount", amount)
        values.put("idStatus", getIDGoalStatus(goalStatus)!!.id)

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
        values.put("idStatus", goal.idStatus)

        if (exists) {
            return
        } else {
            database.insert("goals", null, values)
        }
    }

    fun deleteDatabase() {

        if (databasePath.exists()) {
            databasePath.delete()
        }
    }
}