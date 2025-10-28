package com.example.counterplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.counterplus.ui.theme.CounterPlusTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CounterPlusTheme {
                CounterApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterApp(viewModel: MainViewModel) {
    var showSettings by remember { mutableStateOf(false) }
    val counterState by viewModel.counterState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Counter++") },
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        CounterScreen(
            modifier = Modifier.padding(innerPadding),
            counterState = counterState,
            onIncrement = { viewModel.increment() },
            onDecrement = { viewModel.decrement() },
            onReset = { viewModel.reset() },
            onToggleAuto = { viewModel.toggleAutoMode() }
        )

        if (showSettings) {
            SettingsDialog(
                currentInterval = (counterState.autoIncrementInterval / 1000).toInt(),
                onDismiss = { showSettings = false },
                onIntervalChange = { interval ->
                    viewModel.updateInterval(interval)
                    showSettings = false
                }
            )
        }
    }
}

@Composable
fun CounterScreen(
    modifier: Modifier = Modifier,
    counterState: CounterState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit,
    onToggleAuto: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // auto status
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (counterState.isAutoMode)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Auto mode: ${if (counterState.isAutoMode) "ON" else "OFF"}",
                    fontWeight = FontWeight.Bold
                )
                if (counterState.isAutoMode) {
                    Text(
                        text = "Incrementing every ${counterState.autoIncrementInterval / 1000}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // counter display
        Text(
            text = counterState.count.toString(),
            fontSize = 96.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(48.dp))


// increment and decrement buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDecrement,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("-1", fontSize = 20.sp)
            }


            Button(
                onClick = onIncrement,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("+1", fontSize = 20.sp)
            }
        }




        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }

            Button(
                onClick = onToggleAuto,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary

                )
            ) {
                Text(if (counterState.isAutoMode) "Stop Auto" else "Start Auto")
            }
        }
    }
}

@Composable
fun SettingsDialog(
    currentInterval: Int,
    onDismiss: () -> Unit,
    onIntervalChange: (Int) -> Unit
) {
    var selectedInterval by remember { mutableStateOf(currentInterval) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Auto-Increment Settings") },
        text = {
            Column {
                Text(
                    "Select auto-increment interval:",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                listOf(1, 2, 3, 5, 6, 7, 8, 9, 10).forEach { interval ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedInterval == interval,
                            onClick = { selectedInterval = interval }
                        )
                        Text(
                            text = "$interval ${if (interval == 1) "second" else "seconds"}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },

        confirmButton = {
            TextButton(onClick = { onIntervalChange(selectedInterval) }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}