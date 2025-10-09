package io.oversec.one.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.oversec.one.R
import io.oversec.one.db.Db
import io.oversec.one.db.Padder
import io.oversec.one.ui.viewModel.PaddersViewModel
import io.oversec.one.ui.viewModel.PaddersViewModelFactory
import io.oversec.one.view.PadderDetailActivity

@Composable
fun PaddersScreen(
    db: Db,
    viewModel: PaddersViewModel = viewModel(factory = PaddersViewModelFactory(db))
) {
    val context = LocalContext.current
    val padders by viewModel.padders.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.refreshPadders()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(padders) { padder ->
                PadderListItem(
                    padder = padder,
                    onClick = {
                        val intent = Intent(context, PadderDetailActivity::class.java).apply {
                            putExtra(PadderDetailActivity.EXTRA_ID, padder.id)
                        }
                        launcher.launch(intent)
                    }
                )
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
                    viewModel.addPadder(title, example)
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun PadderListItem(padder: Padder, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
    ) {
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