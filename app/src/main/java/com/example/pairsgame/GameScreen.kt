package com.example.pairsgame

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GameSettings(
    val difficulty: String,
    val columns: Int,
    val rows: Int
) {
    companion object {
        fun getSettingsForDifficulty(difficulty: String): GameSettings {
            return when (difficulty) {
                "Easy" -> GameSettings("Easy", 4, 3)
                "Hard" -> GameSettings("Hard", 6, 8)
                else -> GameSettings("Normal", 4, 6)
            }
        }
    }
}

data class CardItem(
    val id: Int,
    val value: Int,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onStop: () -> Unit,
    difficulty: String,
    onGameComplete: (Int) -> Unit
) {
    val gameSettings = remember(difficulty) { GameSettings.getSettingsForDifficulty(difficulty) }
    var cards by remember(difficulty) { mutableStateOf(generateCards(gameSettings)) }
    var moves by remember { mutableStateOf(0) }
    var pairsFound by remember { mutableStateOf(0) }
    var flippedCards by remember { mutableStateOf<List<CardItem>>(emptyList()) }
    var showWinDialog by remember { mutableStateOf(false) }

    // Check for win condition
    LaunchedEffect(pairsFound) {
        val totalPairs = (gameSettings.columns * gameSettings.rows) / 2
        if (pairsFound == totalPairs) {
            showWinDialog = true
        }
    }

    if (showWinDialog) {
        Dialog(onDismissRequest = { 
            showWinDialog = false
            onGameComplete(moves)
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "YOU WIN!",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Total Moves: $moves",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { 
                            showWinDialog = false
                            onGameComplete(moves)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Back to Home",
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match It - ${gameSettings.difficulty}") },
                actions = {
                    IconButton(onClick = onStop) {
                        Icon(Icons.Default.Delete, contentDescription = "Stop Game")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Game Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(gameSettings.columns),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { card ->
                    GameCard(
                        card = card,
                        onClick = {
                            if (flippedCards.size < 2 && !card.isFlipped && !card.isMatched) {
                                val updatedCards = cards.map {
                                    if (it.id == card.id) it.copy(isFlipped = true)
                                    else it
                                }
                                cards = updatedCards
                                flippedCards = flippedCards + card

                                if (flippedCards.size == 2) {
                                    moves++
                                    if (flippedCards[0].value == flippedCards[1].value) {
                                        pairsFound++
                                        cards = cards.map {
                                            if (it.id == flippedCards[0].id || it.id == flippedCards[1].id)
                                                it.copy(isMatched = true)
                                            else it
                                        }
                                    }
                                    // Reset flipped cards after a delay
                                    kotlinx.coroutines.MainScope().launch {
                                        delay(1000)
                                        cards = cards.map {
                                            if (!it.isMatched) it.copy(isFlipped = false)
                                            else it
                                        }
                                        flippedCards = emptyList()
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Bottom row with counters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pairs counter
                Text(
                    text = "Pairs: $pairsFound",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                // Moves counter
                Text(
                    text = "Moves: $moves",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GameCard(
    card: CardItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick)
            .border(2.dp, Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (card.isFlipped || card.isMatched)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface
                ),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Text(
                    text = card.value.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun generateCards(settings: GameSettings): List<CardItem> {
    val totalPairs = (settings.columns * settings.rows) / 2
    val values = (1..totalPairs).flatMap { listOf(it, it) }
    return values.shuffled().mapIndexed { index, value ->
        CardItem(id = index, value = value)
    }
} 