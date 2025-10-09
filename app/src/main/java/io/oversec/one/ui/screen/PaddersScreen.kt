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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.oversec.one.R
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
            Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.add_padder))
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
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_padder)) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; showError = false },
                    label = { Text(stringResource(R.string.title)) },
                    isError = showError && title.isBlank()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it; showError = false },
                    label = { Text(stringResource(R.string.example)) },
                    isError = showError && example.isBlank()
                )
                if (showError) {
                    Text(
                        text = stringResource(R.string.please_fill_both_fields),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && example.isNotBlank()) {
                        onAdd(title, example)
                    } else {
                        showError = true
                    }
                }
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}