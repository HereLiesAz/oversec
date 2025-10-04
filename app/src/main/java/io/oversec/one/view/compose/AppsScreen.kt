package io.oversec.one.view.compose

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.crypto.AppsReceiver
import io.oversec.one.crypto.Help
import io.oversec.one.db.Db
import io.oversec.one.view.AppConfigActivity
import java.util.*
import kotlin.Comparator

enum class SortOption {
    NAME, CHECKED
}

@Composable
fun AppsScreen() {
    val context = LocalContext.current
    val core = remember { Core.getInstance(context) }
    val db = core.db
    val packageManager = context.packageManager

    var packages by remember { mutableStateOf<List<ApplicationInfo>>(emptyList()) }
    var sortOption by remember { mutableStateOf(SortOption.NAME) }

    val appConfigLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // Reload packages when returning from AppConfigActivity
        packages = getPackages(context, db, packageManager)
    }

    val sortedPackages = remember(packages, sortOption) {
        when (sortOption) {
            SortOption.NAME -> packages.sortedWith(ApplicationInfo.DisplayNameComparator(packageManager))
            SortOption.CHECKED -> packages.sortedWith(Comparator { o1, o2 ->
                val r1 = db.isAppEnabled(o1.packageName)
                val r2 = db.isAppEnabled(o2.packageName)
                var r = r2.compareTo(r1)
                if (r == 0) {
                    val s1 = packageManager.getApplicationLabel(o1)?.toString() ?: o1.packageName
                    val s2 = packageManager.getApplicationLabel(o2)?.toString() ?: o2.packageName
                    r = s1.compareTo(s2)
                }
                r
            })
        }
    }

    fun reloadPackages() {
        packages = getPackages(context, db, packageManager)
    }

    LaunchedEffect(Unit) {
        reloadPackages()
    }

    DisposableEffect(Unit) {
        val listener = object : AppsReceiver.IAppsReceiverListener {
            override fun onAppChanged(ctx: Context, action: String, packagename: String) {
                packages = getPackages(context, db, packageManager)
            }
        }
        AppsReceiver.addListener(listener)
        onDispose { AppsReceiver.removeListener(listener) }
    }

    Column {
        SortOptions(
            selectedOption = sortOption,
            onOptionSelected = { sortOption = it }
        )
        LazyColumn {
            items(sortedPackages) { app ->
                AppListItem(
                    appInfo = app,
                    db = db,
                    packageManager = packageManager,
                    onAppClick = { packageName ->
                        val intent = Intent(context, AppConfigActivity::class.java).apply {
                            putExtra(AppConfigActivity.EXTRA_PACKAGE_NAME, packageName)
                        }
                        appConfigLauncher.launch(intent)
                    },
                    onCheckedChange = { packageName, isChecked ->
                        db.setAppEnabled(packageName, isChecked)
                        reloadPackages()
                    }
                )
            }
        }
    }
}

@Composable
fun SortOptions(selectedOption: SortOption, onOptionSelected: (SortOption) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.main_tab_apps__sort))
        Row {
            RadioButton(
                selected = selectedOption == SortOption.NAME,
                onClick = { onOptionSelected(SortOption.NAME) }
            )
            Text(
                text = stringResource(id = R.string.main_tab_apps__sortbyname),
                modifier = Modifier
                    .padding(start = 4.dp, end = 16.dp)
                    .clickable { onOptionSelected(SortOption.NAME) }
            )
            RadioButton(
                selected = selectedOption == SortOption.CHECKED,
                onClick = { onOptionSelected(SortOption.CHECKED) }
            )
            Text(
                text = stringResource(id = R.string.main_tab_apps__sortbychecked),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable { onOptionSelected(SortOption.CHECKED) }
            )
        }
    }
}

@Composable
fun AppListItem(
    appInfo: ApplicationInfo,
    db: Db,
    packageManager: PackageManager,
    onAppClick: (String) -> Unit,
    onCheckedChange: (String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val appName = packageManager.getApplicationLabel(appInfo).toString()
    val packageName = appInfo.packageName
    val icon = packageManager.getApplicationIcon(appInfo)
    val isChecked = db.isAppEnabled(packageName)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAppClick(packageName) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = icon,
            contentDescription = "App Icon",
            modifier = Modifier.size(48.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(text = appName, style = MaterialTheme.typography.bodyLarge)
            Text(text = packageName, style = MaterialTheme.typography.bodyMedium)
        }
        IconButton(onClick = { Help.openForPackage(context, packageName) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_help_outline_black_24dp),
                contentDescription = "Help"
            )
        }
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(packageName, it) }
        )
    }
}

private fun getPackages(context: Context, db: Db, packageManager: PackageManager): List<ApplicationInfo> {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resInfos = packageManager.queryIntentActivities(intent, 0)
    val packageNames = HashSet<String>()
    for (resolveInfo in resInfos) {
        packageNames.add(resolveInfo.activityInfo.packageName)
    }

    val appInfos = ArrayList<ApplicationInfo>()
    val mIgnore = db.ignoredPackages
    for (packageName in packageNames) {
        if (!mIgnore.contains(packageName)) {
            try {
                appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
            } catch (e: PackageManager.NameNotFoundException) {
                //Do Nothing
            }
        }
    }
    return appInfos
}