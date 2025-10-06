package io.oversec.one.ui.screen.main

import android.text.format.DateUtils
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.crypto.sym.SymUtil
import io.oversec.one.crypto.sym.SymmetricKeyEncrypted
import io.oversec.one.view.KeyDetailsActivity
import java.util.*

@Composable
fun KeysScreen() {
    val context = LocalContext.current
    val keys = remember {
        Core.getInstance(context).db.getSymmetricKeys(false)
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(keys, key = { it.id }) { key ->
            KeyListItem(key = key)
        }
    }
}

@Composable
private fun KeyListItem(key: SymmetricKeyEncrypted) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { KeyDetailsActivity.show(context, key.id) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(name = key.name)
        Spacer(Modifier.width(8.dp))
        ConfirmationIcons(isConfirmed = key.confirmedDate != null)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = key.name, style = MaterialTheme.typography.bodyLarge)
            KeyAgeText(createdDate = key.createdDate)
        }
    }
}

@Composable
private fun Avatar(name: String) {
    val hash = name.hashCode()
    val ba = SymUtil.long2bytearray(hash.toLong())
    val red = ((ba[ba.size - 1].toInt() and 0xFF) * 0.8f).toInt()
    val green = ((ba[ba.size - 2].toInt() and 0xFF) * 0.8f).toInt()
    val blue = ((ba[ba.size - 3].toInt() and 0xFF) * 0.8f).toInt()
    val avatarColor = Color(red, green, blue)

    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color = avatarColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = (name.firstOrNull() ?: ' ').toString(),
            color = Color.White,
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ConfirmationIcons(isConfirmed: Boolean) {
    if (isConfirmed) {
        Image(
            painter = painterResource(id = R.drawable.ic_done_black_24dp),
            contentDescription = "Confirmed",
            modifier = Modifier.size(24.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_warning_black_24dp),
            contentDescription = "Unconfirmed",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun KeyAgeText(createdDate: Date) {
    val context = LocalContext.current
    val now = System.currentTimeMillis()
    val diff = now - createdDate.time
    val days = (diff / (24 * 60 * 60 * 1000)).toInt()

    val color = if (days > 30) {
        Color.Red // Or some other warning color from your theme
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = context.getString(
            R.string.key_age,
            DateUtils.getRelativeTimeSpanString(createdDate.time, now, DateUtils.DAY_IN_MILLIS)
        ),
        color = color,
        style = MaterialTheme.typography.bodySmall
    )
}