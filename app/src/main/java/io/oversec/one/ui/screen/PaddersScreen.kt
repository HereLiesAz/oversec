package io.oversec.one.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.oversec.one.db.Db
import io.oversec.one.db.Padder

@Composable
fun PaddersScreen(db: Db) {
    var padders by remember { mutableStateOf(db.allPadders.toList()) }
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(padders) { padder ->
                PadderListItem(padder = padder)
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Padder")
        }

        if (showAddDialog) {
            AddPadderDialog(
                onDismiss = { showAddDialog = false },
                onAdd = { title, example ->
                    val newPadder = Padder()
                    newPadder.title = title
                    newPadder.example = example
                    db.addPadder(newPadder)
                    padders = db.allPadders.toList() // Ensure a new list is created
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun PadderListItem(padder: Padder) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Text(
                text = padder.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = padder.example,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(3f)
            )
        }
        Divider()
    }
}

@Composable
fun AddPadderDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Padder") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it },
                    label = { Text("Example") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && example.isNotBlank()) {
                        onAdd(title, example)
                    }
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}