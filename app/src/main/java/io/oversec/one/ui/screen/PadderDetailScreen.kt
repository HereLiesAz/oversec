package io.oversec.one.ui.screen

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.oversec.one.R
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.db.PadderDb
import io.oversec.one.ui.viewModel.PadderDetailViewModel
import io.oversec.one.ui.viewModel.PadderDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PadderDetailScreen(
    padderId: Long?,
    db: PadderDb,
    onHelp: () -> Unit,
    onBackPressed: () -> Unit,
    onSaveFinished: () -> Unit,
    viewModel: PadderDetailViewModel = viewModel(factory = PadderDetailViewModelFactory(db, padderId))
) {
    val padder by viewModel.padder.collectAsState()
    var name by remember { mutableStateOf(padder?.name ?: "") }
    var content by remember { mutableStateOf(padder?.content ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var contentError by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    val nameMinLengthError = stringResource(R.string.error_padder_name_too_short, 5)
    val contentMinLengthError = stringResource(R.string.error_padder_content_too_short, 5)
    val context = LocalContext.current

    LaunchedEffect(padder) {
        padder?.let {
            name = it.name
            content = it.content
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_activity_padder_detail)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back_content_description))
                    }
                },
                actions = {
                    IconButton(onClick = onHelp) {
                        Icon(Icons.Filled.HelpOutline, contentDescription = stringResource(R.string.action_help))
                    }
                    if (padderId != null) {
                        IconButton(onClick = { showMenu = !showMenu }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options_content_description))
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.action_delete_key)) },
                                onClick = {
                                    viewModel.deletePadder(context as Activity, onSaveFinished)
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
                    viewModel.savePadder(name, content, context as Activity, onSaveFinished)
                }
            }) {
                Icon(Icons.Filled.Save, contentDescription = stringResource(R.string.action_save))
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
