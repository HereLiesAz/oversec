package io.oversec.one.view.compose

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.oversec.one.R
import io.oversec.one.Util
import io.oversec.one.common.MainPreferences

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(MainPreferences.FILENAME, 0) }

    var screenOffPanic by remember { mutableStateOf(prefs.getBoolean(context.getString(R.string.mainprefs_screenoffpanic_key), false)) }
    var hideLauncherOnPanic by remember { mutableStateOf(prefs.getBoolean(context.getString(R.string.mainprefs_hidelauncheronpanic_key), false)) }
    var relaxCache by remember { mutableStateOf(prefs.getBoolean(context.getString(R.string.mainprefs_relaxecache_key), false)) }
    var allowScreenshots by remember { mutableStateOf(prefs.getBoolean(context.getString(R.string.mainprefs_allow_screenshot_key), false)) }
    var launcherSecretCode by remember { mutableStateOf(prefs.getString(context.getString(R.string.mainprefs_launchersecretcode_key), "") ?: "") }

    var showDialerNotConfirmedDialog by remember { mutableStateOf(false) }
    var showSetCodeFirstDialog by remember { mutableStateOf(false) }

    val listener = remember {
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            when (key) {
                context.getString(R.string.mainprefs_screenoffpanic_key) -> screenOffPanic = sharedPreferences.getBoolean(key, false)
                context.getString(R.string.mainprefs_hidelauncheronpanic_key) -> hideLauncherOnPanic = sharedPreferences.getBoolean(key, false)
                context.getString(R.string.mainprefs_relaxecache_key) -> relaxCache = sharedPreferences.getBoolean(key, false)
                context.getString(R.string.mainprefs_allow_screenshot_key) -> allowScreenshots = sharedPreferences.getBoolean(key, false)
                context.getString(R.string.mainprefs_launchersecretcode_key) -> launcherSecretCode = sharedPreferences.getString(key, "") ?: ""
            }
        }
    }

    DisposableEffect(Unit) {
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    if (showDialerNotConfirmedDialog) {
        AlertDialog(
            onDismissRequest = { showDialerNotConfirmedDialog = false },
            title = { Text(stringResource(R.string.mainprefs_hidelauncheronpanic_title)) },
            text = { Text(stringResource(R.string.secretdialer_code_needs_to_be_set_up)) },
            confirmButton = {
                Button(onClick = { showDialerNotConfirmedDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        )
    }

    if (showSetCodeFirstDialog) {
        AlertDialog(
            onDismissRequest = { showSetCodeFirstDialog = false },
            title = { Text("Secret Code Required") }, // TODO: Use string resource
            text = { Text("Please set a secret launcher code before enabling panic features.") },
            confirmButton = {
                Button(onClick = { showSetCodeFirstDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        CheckboxPreference(
            title = stringResource(id = R.string.mainprefs_screenoffpanic_title),
            summary = stringResource(id = R.string.mainprefs_screenoffpanic_summary),
            checked = screenOffPanic,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    if (launcherSecretCode.isEmpty()) {
                        showSetCodeFirstDialog = true
                    } else {
                        prefs.edit().putBoolean(context.getString(R.string.mainprefs_screenoffpanic_key), true).apply()
                    }
                } else {
                    prefs.edit().putBoolean(context.getString(R.string.mainprefs_screenoffpanic_key), false).apply()
                }
            }
        )
        if (Util.hasDialerIntentHandler(context)) {
            CheckboxPreference(
                title = stringResource(id = R.string.mainprefs_hidelauncheronpanic_title),
                summary = stringResource(id = R.string.mainprefs_hidelauncheronpanic_summary),
                checked = hideLauncherOnPanic,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        if (!MainPreferences.isDialerSecretCodeBroadcastConfirmedWorking(context)) {
                            showDialerNotConfirmedDialog = true
                        } else if (launcherSecretCode.isEmpty()) {
                            showSetCodeFirstDialog = true
                        } else {
                            prefs.edit().putBoolean(context.getString(R.string.mainprefs_hidelauncheronpanic_key), true).apply()
                        }
                    } else {
                        prefs.edit().putBoolean(context.getString(R.string.mainprefs_hidelauncheronpanic_key), false).apply()
                    }
                }
            )
            TextPreference(
                title = stringResource(id = R.string.mainprefs_launchersecretcode_title),
                summary = stringResource(id = R.string.mainprefs_launchersecretcode_summary),
                value = launcherSecretCode,
                onValueChange = {
                    prefs.edit().putString(context.getString(R.string.mainprefs_launchersecretcode_key), it).apply()
                }
            )
        }
        if (context.resources.getBoolean(R.bool.feature_expert_options)) {
            CheckboxPreference(
                title = stringResource(id = R.string.mainprefs_relaxcache_title),
                summary = stringResource(id = R.string.mainprefs_relaxcache_summary),
                checked = relaxCache,
                onCheckedChange = { prefs.edit().putBoolean(context.getString(R.string.mainprefs_relaxecache_key), it).apply() }
            )
        }
        CheckboxPreference(
            title = stringResource(id = R.string.mainprefs_allow_screenshot_title),
            summary = stringResource(id = R.string.mainprefs_allow_screenshot_summary),
            checked = allowScreenshots,
            onCheckedChange = { prefs.edit().putBoolean(context.getString(R.string.mainprefs_allow_screenshot_key), it).apply() }
        )
    }
}

@Composable
fun CheckboxPreference(title: String, summary: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodyMedium)
        }
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun TextPreference(title: String, summary: String, value: String, onValueChange: (String) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(value) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(vertical = 8.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Text(text = summary, style = MaterialTheme.typography.bodyMedium)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = title) },
            text = {
                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text("Secret Code") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onValueChange(textValue)
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}