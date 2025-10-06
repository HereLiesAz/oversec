package io.oversec.one.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.db.PadderDb
import io.oversec.one.view.PadderDetailActivity

@Composable
fun PadderScreen() {
    val context = LocalContext.current
    val db = remember { PadderDb.getInstance(context) }
    val padders = remember { db.allValues }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(padders, key = { it.key }) { padder ->
                PadderListItem(padder = padder) {
                    PadderDetailActivity.showForResult(context, 1, padder.key)
                }
                Divider()
            }
        }
        FloatingActionButton(
            onClick = { PadderDetailActivity.showForResult(context, 1, null) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Padder")
        }
    }
}

@Composable
private fun PadderListItem(padder: PadderContent, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = padder.name, style = MaterialTheme.typography.titleMedium)
            Text(text = padder.contentBegin, style = MaterialTheme.typography.bodyMedium)
        }
    }
}