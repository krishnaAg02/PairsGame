package com.example.pairsgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.pairsgame.ui.theme.PairsGameTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var leaderboardManager: LeaderboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        leaderboardManager = LeaderboardManager(this)
        enableEdgeToEdge()
        setContent {
            PairsGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by remember { mutableStateOf("home") }
                    var currentDifficulty by remember { mutableStateOf("Normal") }
                    var leaderboardData by remember { 
                        mutableStateOf(leaderboardManager.loadScores())
                    }

                    fun addScore(difficulty: String, moves: Int) {
                        val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
                        val newEntry = LeaderboardEntry(difficulty, moves, date)
                        val currentScores = leaderboardData[difficulty] ?: emptyList()
                        val updatedScores = (currentScores + newEntry)
                            .sortedBy { it.moves }
                            .take(3)
                        leaderboardData = leaderboardData + (difficulty to updatedScores)
                        leaderboardManager.saveScores(leaderboardData)
                    }

                    when (currentScreen) {
                        "home" -> HomeScreen(
                            onStartGame = { currentScreen = "game" },
                            onSettings = { currentScreen = "settings" },
                            onLeaderboard = { currentScreen = "leaderboard" }
                        )
                        "game" -> GameScreen(
                            onStop = { currentScreen = "home" },
                            difficulty = currentDifficulty,
                            onGameComplete = { moves -> 
                                addScore(currentDifficulty, moves)
                                currentScreen = "home"
                            }
                        )
                        "settings" -> SettingsScreen(
                            onBack = { currentScreen = "home" },
                            onDifficultyChanged = { difficulty ->
                                currentDifficulty = difficulty
                            }
                        )
                        "leaderboard" -> LeaderboardScreen(
                            onBack = { currentScreen = "home" },
                            leaderboardData = leaderboardData
                        )
                    }
                }
            }
        }
    }
}
