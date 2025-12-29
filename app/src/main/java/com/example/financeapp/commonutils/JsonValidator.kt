package com.example.financeapp.commonutils

import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

fun isValidJson(json: String): Any {

    return try {
        when {
            json.trim().startsWith("{") -> JSONObject(json)
            json.trim().startsWith("[") -> JSONArray(json)
            else -> false
        }

    } catch (e: Exception) {
        return false
    }
}