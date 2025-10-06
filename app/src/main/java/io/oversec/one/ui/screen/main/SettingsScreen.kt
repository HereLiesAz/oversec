package io.oversec.one.ui.screen.main

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
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
import io.oversec.one.view.MainSettingsFragment

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        CheckboxPreference(
            title = stringResource(R.string.mainprefs_screenoffpanic_title),
            summary = stringResource(R.string.mainprefs_screenoffpanic_summary),
            key = stringResource(R.string.mainprefs_screenoffpanic_key)
        )
        CheckboxPreference(
            title = stringResource(R.string.mainprefs_hidelauncheronpanic_title),
            summary = stringResource(R.string.mainprefs_hidelauncheronpanic_summary),
            key = stringResource(R.string.mainprefs_hidelauncheronpanic_key)
        )
        if (Util.hasDialerIntentHandler(context)) {
            DialerCodePreference(
                title = stringResource(R.string.mainprefs_launchersecretcode_title),
                summary = stringResource(R.string.mainprefs_launchersecretcode_summary),
                key = stringResource(R.string.mainprefs_launchersecretcode_key)
            )
        }
        if (context.resources.getBoolean(R.bool.feature_expert_options)) {
            CheckboxPreference(
                title = stringResource(R.string.mainprefs_relaxcache_title),
                summary = stringResource(R.string.mainprefs_relaxcache_summary),
                key = stringResource(R.string.mainprefs_relaxecache_key)
            )
        }
        CheckboxPreference(
            title = stringResource(R.string.mainprefs_allow_screenshot_title),
            summary = stringResource(R.string.mainprefs_allow_screenshot_summary),
            key = stringResource(R.string.mainprefs_allow_screenshot_key)
        )
    }
}

@Composable
private fun CheckboxPreference(title: String, summary: String, key: String) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(MainPreferences.FILENAME, Context.MODE_PRIVATE) }
    var isChecked by remember { mutableStateOf(prefs.getBoolean(key, false)) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val newValue = !isChecked
                if (key == context.getString(R.string.mainprefs_hidelauncheronpanic_key) && newValue) {
                    if (!MainPreferences.isDialerSecretCodeBroadcastConfirmedWorking(context)) {
                        dialogText = context.getString(R.string.secretdialer_code_needs_to_be_set_up)
                        showDialog = true
                        return@clickable
                    }
                }

                isChecked = newValue
                prefs.edit().putBoolean(key, newValue).apply()

                if ((key == context.getString(R.string.mainprefs_hidelauncheronpanic_key) && newValue) ||
                    (key == context.getString(R.string.mainprefs_screenoffpanic_key) && newValue)
                ) {
                    var code = MainPreferences.getLauncherSecretDialerCode(context)
                    if (code.isEmpty()) {
                        code = (Math.random() * 100000).toInt().toString().substring(0, 5)
                        MainPreferences.setLauncherSecretDialerCode(context, code)
                    }
                    if (Util.hasDialerIntentHandler(context)) {
                        dialogText = context.getString(R.string.secretdialer_code, code)
                        showDialog = true
                    }
                }
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodySmall)
        }
        Checkbox(checked = isChecked, onCheckedChange = null) // Click handled by Row
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(title) },
            text = { Text(dialogText) },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun DialerCodePreference(title: String, summary: String, key: String) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(MainPreferences.FILENAME, Context.MODE_PRIVATE) }
    var code by remember { mutableStateOf(prefs.getString(key, "") ?: "") }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showDialog) {
        var tempCode by remember { mutableStateOf(code) }
        var isError by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(title) },
            text = {
                Column {
                    OutlinedTextField(
                        value = tempCode,
                        onValueChange = {
                            tempCode = it
                            isError = it.length < MainSettingsFragment.MIN_SECRETCODE_LENGTH
                        },
                        label = { Text(stringResource(R.string.mainprefs_launchersecretcode_title)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isError,
                        singleLine = true
                    )
                    if(isError) {
                        Text(
                            text = stringResource(R.string.secretdialer_code_too_short, MainSettingsFragment.MIN_SECRETCODE_LENGTH),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempCode.length >= MainSettingsFragment.MIN_SECRETCODE_LENGTH) {
                            code = tempCode
                            prefs.edit().putString(key, code).apply()
                            showDialog = false
                            showConfirmationDialog = true
                        } else {
                            isError = true
                        }
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

    if (showConfirmationDialog) {
        var dialogText = context.getString(R.string.secretdialer_code, code)
        if (!MainPreferences.isDialerSecretCodeBroadcastConfirmedWorking(context)) {
            dialogText += "\n\n" + context.getString(R.string.secretdialer_code_initial_confirm)
        }

        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text(stringResource(R.string.mainprefs_launchersecretcode_title)) },
            text = { Text(dialogText) },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Intent.ACTION_DIAL)
                    context.startActivity(intent)
                    showConfirmationDialog = false
                }) {
                    Text(stringResource(R.string.action_show_dialer))
                }
            },
            dismissButton = {
                Button(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}