package io.oversec.one.view.compose

import android.app.Activity
import android.content.Intent
import android.text.format.DateUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oversec.one.R
import io.oversec.one.crypto.sym.OversecKeystore2
import io.oversec.one.crypto.sym.SymmetricKeyEncrypted
import io.oversec.one.view.KeyDetailsActivity
import io.oversec.one.view.KeyImportCreateActivity
import io.oversec.one.view.util.SymUIUtil
import java.util.*

private const val RQ_CREATE_NEW_KEY = 7007
private const val EXTRA_KEY_ID = "EXTRA_KEY_ID"

@Composable
fun KeysScreen() {
    val context = LocalContext.current
    val keystore = remember { OversecKeystore2.getInstance(context) }
    var keys by remember { mutableStateOf(keystore.encryptedKeys_sorted) }

    val keyDetailsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        keys = keystore.encryptedKeys_sorted
    }

    val keyCreateLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val keyId = it.getLongExtra(EXTRA_KEY_ID, 0)
                if (keyId != 0L) {
                    val detailsIntent = KeyDetailsActivity.getShowIntent(context, keyId)
                    keyDetailsLauncher.launch(detailsIntent)
                }
            }
        } else {
            keys = keystore.encryptedKeys_sorted
        }
    }

    DisposableEffect(keystore) {
        val listener = object : OversecKeystore2.KeyStoreListener {
            override fun onKeyStoreChanged() {
                keys = keystore.encryptedKeys_sorted
            }
        }
        keystore.addListener(listener)
        onDispose { keystore.removeListener(listener) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(keys) { key ->
                KeyListItem(key = key) {
                    KeyDetailsActivity.show(context, it)
                }
            }
        }
        var showAddKeyDialog by remember { mutableStateOf(false) }

        FloatingActionButton(
            onClick = { showAddKeyDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_black_24dp),
                contentDescription = "Add Key"
            )
        }

        if (showAddKeyDialog) {
            AddKeyDialog(
                onDismiss = { showAddKeyDialog = false },
                onOptionSelected = { mode ->
                    showAddKeyDialog = false
                    val intent = Intent(context, KeyImportCreateActivity::class.java).apply {
                        putExtra("mode", mode)
                    }
                    keyCreateLauncher.launch(intent)
                }
            )
        }
    }
}

@Composable
fun AddKeyDialog(
    onDismiss: () -> Unit,
    onOptionSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.title_dialog_add_key)) },
        text = {
            Column {
                Text(
                    text = stringResource(id = R.string.action_addkey_importqr_title),
                    modifier = Modifier.clickable { onOptionSelected("scan") }.fillMaxWidth().padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.action_createkey_pbe_title),
                    modifier = Modifier.clickable { onOptionSelected("pbkdf") }.fillMaxWidth().padding(vertical = 8.dp)
                )
                Text(
                    text = stringResource(id = R.string.action_createkey_random_title),
                    modifier = Modifier.clickable { onOptionSelected("random") }.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = android.R.string.cancel))
            }
        }
    )
}

@Composable
fun KeyListItem(key: SymmetricKeyEncrypted, onClick: (Long) -> Unit) {
    val context = LocalContext.current
    val confirmDate = key.confirmedDate
    val createdDate = key.createdDate
    val now = Date()
    val ageInMillis = now.time - createdDate.time
    val ageInDays = (ageInMillis / (24 * 60 * 60 * 1000)).toInt()
    val ageString = DateUtils.getRelativeTimeSpanString(createdDate.time, now.time, DateUtils.DAY_IN_MILLIS)

    val ageColor = if (ageInDays > 30) {
        MaterialTheme.colorScheme.error
    } else {
        LocalContentColor.current
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(key.id) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatarText = key.name.firstOrNull()?.toString() ?: "?"
        val avatarColor = remember(key.name) { SymUIUtil.colorFor(key.name) }
        Text(
            text = avatarText,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(avatarColor))
                .wrapContentHeight(Alignment.CenterVertically)
        )

        Spacer(modifier = Modifier.width(8.dp))

        if (confirmDate == null) {
            Icon(
                painter = painterResource(id = R.drawable.ic_warning_black_24dp),
                contentDescription = "Unconfirmed",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_done_black_24dp),
                contentDescription = "Confirmed",
                tint = Color(0xFF00C853),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = key.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = stringResource(R.string.key_age, ageString),
                style = MaterialTheme.typography.bodyMedium,
                color = ageColor
            )
        }
    }
}