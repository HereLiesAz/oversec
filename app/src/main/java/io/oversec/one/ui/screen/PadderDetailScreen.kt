package io.oversec.one.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.oversec.one.R
import io.oversec.one.db.PadderContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PadderDetailScreen(
    padder: PadderContent?,
    onSave: (String, String) -> Unit,
    onDelete: () -> Unit,
    onHelp: () -> Unit,
    onBackPressed: () -> Unit
) {
    var name by remember { mutableStateOf(padder?.name ?: "") }
    var content by remember { mutableStateOf(padder?.content ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    val nameMinLengthError = stringResource(R.string.error_padder_name_too_short, 5)
    val contentMinLengthError = stringResource(R.string.error_padder_content_too_short, 5)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_activity_padder_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_content_description))
                    }
                },
                actions = {
                    IconButton(onClick = onHelp) {
                        Icon(Icons.Default.HelpOutline, contentDescription = stringResource(R.string.action_help))
                    }
                    if (padder != null) {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.more_options_content_description))
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.action_delete)) },
                                onClick = {
                                    onDelete()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                nameError = if (name.length < 5) nameMinLengthError else null
                contentError = if (content.length < 5) contentMinLengthError else null
                if (nameError == null && contentError == null) {
                    onSave(name, content)
                }
            }) {
                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.action_save))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = null },
                label = { Text(stringResource(R.string.label_padder_name)) },
                isError = nameError != null,
                modifier = Modifier.fillMaxWidth()
            )
            if (nameError != null) {
                Text(nameError!!, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it; contentError = null },
                label = { Text(stringResource(R.string.label_padder_content)) },
                isError = contentError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            if (contentError != null) {
                Text(contentError!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}