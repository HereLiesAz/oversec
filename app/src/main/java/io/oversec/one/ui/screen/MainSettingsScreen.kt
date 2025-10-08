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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.oversec.one.Core
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.db.Db

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: android.graphics.drawable.Drawable,
    var isEnabled: Boolean
)

private fun isPackageInstalled(pm: PackageManager, packageName: String): Boolean {
    return try {
        pm.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

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
    onOkcFdroidClick: () -> Unit
) {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val core = Core.getInstance(context)

    var apps by remember {
        mutableStateOf(
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 || it.packageName == "com.android.chrome" }
                .map {
                    AppInfo(
                        appName = it.loadLabel(packageManager).toString(),
                        packageName = it.packageName,
                        icon = it.loadIcon(packageManager),
                        isEnabled = db.isPackageEnabled(it.packageName)
                    )
                }
        )
    }

    var sortOption by remember { mutableStateOf("Name") }

    val sortedApps = remember(apps, sortOption) {
        when (sortOption) {
            "Name" -> apps.sortedBy { it.appName.lowercase() }
            "Checked" -> apps.sortedByDescending { it.isEnabled }
            else -> apps
        }
    }

    var isAcsEnabled by remember { mutableStateOf(core.isAccessibilityServiceRunning) }
    var isBossModeActive by remember { mutableStateOf(db.isBossMode) }
    var isOkcInstalled by remember { mutableStateOf(isPackageInstalled(packageManager, OpenKeychainConnector.PACKAGE_NAME)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                isAcsEnabled = core.isAccessibilityServiceRunning
                isBossModeActive = db.isBossMode
                isOkcInstalled = isPackageInstalled(packageManager, OpenKeychainConnector.PACKAGE_NAME)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                Text("Sort by:")
                RadioButton(
                    selected = sortOption == "Name",
                    onClick = { sortOption = "Name" }
                )
                Text("Name")
                RadioButton(
                    selected = sortOption == "Checked",
                    onClick = { sortOption = "Checked" }
                )
                Text("Checked")
            }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
        }

        items(sortedApps, key = { it.packageName }) { app ->
            AppListItem(
                appInfo = app,
                onCheckedChange = { newCheckedState ->
                    db.setPackageEnabled(app.packageName, newCheckedState)
                    // Create a new list to trigger recomposition
                    apps = apps.map {
                        if (it.packageName == app.packageName) it.copy(isEnabled = newCheckedState) else it
                    }
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
            contentDescription = "App Icon",
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = appInfo.appName, style = MaterialTheme.typography.titleMedium)
            Text(text = appInfo.packageName, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onHelpClick) {
            Icon(Icons.Filled.HelpOutline, contentDescription = "Help")
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
                title = "Accessibility Service",
                status = "Service not enabled.",
                button2Text = "Configure",
                onButton2Click = onAcsConfigureClick
            )
            Spacer(Modifier.height(8.dp))
        }
        if (isBossModeActive) {
            InfoCard(
                title = "Boss Key",
                status = "Boss/Panic mode is active.",
                button2Text = "Re-enable",
                onButton2Click = onBossKeyConfigureClick
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(onClick = onHelpClick, modifier = Modifier.fillMaxWidth()) { Text("Help") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onBugReportClick, modifier = Modifier.fillMaxWidth()) { Text("Send Bug Report") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onShareClick, modifier = Modifier.fillMaxWidth()) { Text("Share App") }
        Spacer(Modifier.height(16.dp))

        InfoCard(
            title = "Upgrade to Full Version",
            status = "Unlock all features",
            button2Text = "Upgrade Now",
            onButton2Click = onUpgradeClick
        )
        Spacer(Modifier.height(8.dp))
        if (!isOkcInstalled) {
            InfoCard(
                title = "OpenKeychain",
                status = "Not installed. Required for PGP.",
                button2Text = "Play Store",
                button3Text = "F-Droid",
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