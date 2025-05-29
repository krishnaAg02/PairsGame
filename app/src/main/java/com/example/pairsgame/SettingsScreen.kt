package com.example.pairsgame

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onDifficultyChanged: (String) -> Unit
) {
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }
    var difficulty by remember { mutableStateOf("Normal") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Game Settings",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Sound Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Sound Effects")
                        Switch(
                            checked = soundEnabled,
                            onCheckedChange = { soundEnabled = it }
                        )
                    }

                    // Vibration Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Vibration")
                        Switch(
                            checked = vibrationEnabled,
                            onCheckedChange = { vibrationEnabled = it }
                        )
                    }

                    Divider()

                    // Difficulty Selection
                    Text(
                        "Difficulty",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                difficulty = "Easy"
                                onDifficultyChanged("Easy")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "Easy") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Easy")
                        }
                        
                        Button(
                            onClick = {
                                difficulty = "Normal"
                                onDifficultyChanged("Normal")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "Normal") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Normal")
                        }
                        
                        Button(
                            onClick = {
                                difficulty = "Hard"
                                onDifficultyChanged("Hard")
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (difficulty == "Hard") 
                                    MaterialTheme.colorScheme.primary 
                                else 
                                    MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Hard")
                        }
                    }
                }
            }
        }
    }
} 