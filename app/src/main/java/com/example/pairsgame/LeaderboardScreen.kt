package com.example.pairsgame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LeaderboardEntry(
    val difficulty: String,
    val moves: Int,
    val date: String
)

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit,
    leaderboardData: Map<String, List<LeaderboardEntry>>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Text("←", fontSize = 24.sp)
            }
            Text(
                text = "Leaderboard",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Leaderboard Content
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            listOf("Easy", "Normal", "Hard").forEach { difficulty ->
                item {
                    LeaderboardSection(
                        difficulty = difficulty,
                        entries = leaderboardData[difficulty] ?: emptyList()
                    )
                }
            }
        }
    }
}

@Composable
fun LeaderboardSection(
    difficulty: String,
    entries: List<LeaderboardEntry>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = difficulty,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (entries.isEmpty()) {
                Text(
                    text = "No scores yet",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                entries.sortedBy { it.moves }.forEachIndexed { index, entry ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Medal or rank
                        Text(
                            text = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "#${index + 1}"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Moves
                        Text(
                            text = "${entry.moves} moves",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                        
                        // Date
                        Text(
                            text = entry.date,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
} 