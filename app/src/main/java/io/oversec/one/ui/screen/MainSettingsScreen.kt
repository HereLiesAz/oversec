package io.oversec.one.ui.screen

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.oversec.one.R
import io.oversec.one.db.Db
import io.oversec.one.ui.viewModel.MainSettingsViewModel
import io.oversec.one.ui.viewModel.MainSettingsViewModelFactory

@Composable
fun MainSettingsScreen(
    db: Db,
    onAppHelpClick: (String) -> Unit,
    onHelpClick: () -> Unit,
    onBugReportClick: () -> Unit,
    onShareClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    onAcsConfigureClick: () -> Unit,
    onBossKeyConfigureClick: () -> Unit,
    onOkcPlayStoreClick: () -> Unit,
    onOkcFdroidClick: () -> Unit,
    viewModel: MainSettingsViewModel = viewModel(factory = MainSettingsViewModelFactory(db, LocalContext.current))
) {
    val apps by viewModel.apps.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val isAcsEnabled by viewModel.isAcsEnabled.collectAsState()
    val isBossModeActive by viewModel.isBossModeActive.collectAsState()
    val isOkcInstalled by viewModel.isOkcInstalled.collectAsState()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val sortedApps = remember(apps, sortOption) {
        when (sortOption) {
            "Name" -> apps.sortedBy { it.appName.lowercase() }
            "Checked" -> apps.sortedWith(
                compareByDescending<AppInfo> { it.isEnabled }
                    .thenBy { it.appName.lowercase() }
            )
            else -> apps
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            MainHelpSection(
                isAcsEnabled = isAcsEnabled,
                isBossModeActive = isBossModeActive,
                isOkcInstalled = isOkcInstalled,
                onHelpClick = onHelpClick,
                onBugReportClick = onBugReportClick,
                onShareClick = onShareClick,
                onUpgradeClick = onUpgradeClick,
                onAcsConfigureClick = onAcsConfigureClick,
                onBossKeyConfigureClick = onBossKeyConfigureClick,
                onOkcPlayStoreClick = onOkcPlayStoreClick,
                onOkcFdroidClick = onOkcFdroidClick
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.main_tab_apps__sort))
                RadioButton(
                    selected = sortOption == "Name",
                    onClick = { viewModel.onSortOptionChange("Name") }
                )
                Text(stringResource(R.string.main_tab_apps__sortbyname))
                RadioButton(
                    selected = sortOption == "Checked",
                    onClick = { viewModel.onSortOptionChange("Checked") }
                )
                Text(stringResource(R.string.main_tab_apps__sortbychecked))
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        items(sortedApps, key = { it.packageName }) { app ->
            AppListItem(
                appInfo = app,
                onCheckedChange = { newCheckedState ->
                    viewModel.onAppCheckedChange(app.packageName, newCheckedState)
                },
                onHelpClick = { onAppHelpClick(app.packageName) }
            )
        }
    }
}

@Composable
fun AppListItem(
    appInfo: AppInfo,
    onCheckedChange: (Boolean) -> Unit,
    onHelpClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberDrawablePainter(drawable = appInfo.icon),
            contentDescription = stringResource(R.string.app_icon_description),
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = appInfo.appName, style = MaterialTheme.typography.titleMedium)
            Text(text = appInfo.packageName, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onHelpClick) {
            Icon(Icons.Filled.HelpOutline, contentDescription = stringResource(R.string.action_help))
        }
        Checkbox(
            checked = appInfo.isEnabled,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun MainHelpSection(
    isAcsEnabled: Boolean,
    isBossModeActive: Boolean,
    isOkcInstalled: Boolean,
    onHelpClick: () -> Unit,
    onBugReportClick: () -> Unit,
    onShareClick: () -> Unit,
    onUpgradeClick: () -> Unit,
    onAcsConfigureClick: () -> Unit,
    onBossKeyConfigureClick: () -> Unit,
    onOkcPlayStoreClick: () -> Unit,
    onOkcFdroidClick: () -> Unit
) {
    Column {
        if (!isAcsEnabled) {
            InfoCard(
                title = stringResource(R.string.accessibility_service),
                status = stringResource(R.string.service_not_enabled),
                button2Text = stringResource(R.string.action_configure),
                onButton2Click = onAcsConfigureClick
            )
            Spacer(Modifier.height(8.dp))
        }
        if (isBossModeActive) {
            InfoCard(
                title = stringResource(R.string.boss_key),
                status = stringResource(R.string.boss_panic_mode_active),
                button2Text = stringResource(R.string.action_reenable_boss),
                onButton2Click = onBossKeyConfigureClick
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(onClick = onHelpClick, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.action_help)) }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBugReportClick, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.action_send_bugreport)) }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onShareClick, modifier = Modifier.fillMaxWidth()) { Text(stringResource(R.string.action_share_app)) }
        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = stringResource(R.string.upgrade_to_full_version),
            status = stringResource(R.string.unlock_all_features),
            button2Text = stringResource(R.string.action_upgrade),
            onButton2Click = onUpgradeClick
        )
        Spacer(Modifier.height(8.dp))
        if (!isOkcInstalled) {
            InfoCard(
                title = stringResource(R.string.openkeychain),
                status = stringResource(R.string.not_installed_pgp),
                button2Text = stringResource(R.string.okc_install_googleplay),
                button3Text = stringResource(R.string.okc_install_fdroid),
                onButton2Click = onOkcPlayStoreClick,
                onButton3Click = onOkcFdroidClick
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    status: String,
    button1Text: String? = null,
    onButton1Click: (() -> Unit)? = null,
    button2Text: String? = null,
    onButton2Click: (() -> Unit)? = null,
    button3Text: String? = null,
    onButton3Click: (() -> Unit)? = null
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(text = status, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.align(Alignment.End)) {
                if (button1Text != null && onButton1Click != null) {
                    TextButton(onClick = onButton1Click) { Text(button1Text) }
                }
                if (button2Text != null && onButton2Click != null) {
                    TextButton(onClick = onButton2Click) { Text(button2Text) }
                }
                if (button3Text != null && onButton3Click != null) {
                    TextButton(onClick = onButton3Click) { Text(button3Text) }
                }
            }
        }
    }
}