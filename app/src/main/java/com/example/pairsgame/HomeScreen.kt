package com.example.pairsgame

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.app.Activity
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
    onLeaderboard: () -> Unit
) {
    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Match It") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Match It",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onStartGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Start Game", fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Settings", fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onLeaderboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Leaderboard", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { activity.finish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Exit Game", fontSize = 18.sp)
            }
        }
    }
} 