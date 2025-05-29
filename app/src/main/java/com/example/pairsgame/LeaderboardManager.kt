package com.example.pairsgame

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LeaderboardManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("leaderboard", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveScores(scores: Map<String, List<LeaderboardEntry>>) {
        val json = gson.toJson(scores)
        sharedPreferences.edit().putString("scores", json).apply()
    }

    fun loadScores(): Map<String, List<LeaderboardEntry>> {
        val json = sharedPreferences.getString("scores", null) ?: return emptyMap()
        val type = object : TypeToken<Map<String, List<LeaderboardEntry>>>() {}.type
        return gson.fromJson(json, type)
    }
} 