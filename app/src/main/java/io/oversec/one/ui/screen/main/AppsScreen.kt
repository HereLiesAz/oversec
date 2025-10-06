package io.oversec.one.ui.screen.main

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.crypto.Help
import io.oversec.one.db.Db
import io.oversec.one.view.AppConfigActivity
import java.util.*

private enum class SortOrder {
    NAME, CHECKED
}

@Composable
fun AppsScreen() {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val db = Core.getInstance(context).db

    var sortOrder by remember { mutableStateOf(SortOrder.NAME) }
    var appList by remember { mutableStateOf(getPackages(context, packageManager, db)) }

    LaunchedEffect(sortOrder) {
        val sortedList = appList.toMutableList()
        applySort(sortedList, sortOrder, packageManager, db)
        appList = sortedList
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SortControls(
            selectedOrder = sortOrder,
            onOrderChange = { sortOrder = it }
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(appList, key = { it.packageName }) { appInfo ->
                AppListItem(
                    appInfo = appInfo,
                    packageManager = packageManager,
                    db = db,
                    onClick = {
                        AppConfigActivity.showForResult(context, 1, appInfo.packageName)
                    },
                    onHelpClick = {
                        Help.INSTANCE.openForPackage(context, appInfo.packageName)
                    },
                    onCheckedChange = { isChecked ->
                        db.setAppEnabled(appInfo.packageName, isChecked)
                        // This is a bit of a hack to trigger a recomposition of the list
                        // A better solution would use a more reactive data source.
                        val newList = appList.toMutableList()
                        applySort(newList, sortOrder, packageManager, db)
                        appList = newList
                    }
                )
            }
        }
    }
}

@Composable
private fun SortControls(selectedOrder: SortOrder, onOrderChange: (SortOrder) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(R.string.main_tab_apps__sort))
        Spacer(Modifier.width(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOrder == SortOrder.NAME,
                onClick = { onOrderChange(SortOrder.NAME) }
            )
            Text(stringResource(R.string.main_tab_apps__sortbyname))
        }
        Spacer(Modifier.width(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOrder == SortOrder.CHECKED,
                onClick = { onOrderChange(SortOrder.CHECKED) }
            )
            Text(stringResource(R.string.main_tab_apps__sortbychecked))
        }
    }
}

@Composable
private fun AppListItem(
    appInfo: ApplicationInfo,
    packageManager: PackageManager,
    db: Db,
    onClick: () -> Unit,
    onHelpClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(db.isAppEnabled(appInfo.packageName)) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = try {
            packageManager.getApplicationIcon(appInfo.packageName).toBitmap().asImageBitmap()
        } catch (e: Exception) {
            null
        }
        if (icon != null) {
            Image(bitmap = icon, contentDescription = null, modifier = Modifier.size(48.dp))
        } else {
            Spacer(modifier = Modifier.size(48.dp))
        }

        Spacer(Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = packageManager.getApplicationLabel(appInfo).toString(),
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = appInfo.packageName,
                style = MaterialTheme.typography.bodySmall
            )
        }

        IconButton(onClick = onHelpClick) {
            Icon(painterResource(R.drawable.ic_help_outline_black_24dp), contentDescription = "Help")
        }

        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                isChecked = it
                onCheckedChange(it)
            }
        )
    }
}

private fun getPackages(context: Context, packageManager: PackageManager, db: Db): List<ApplicationInfo> {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val resInfos: List<ResolveInfo> = packageManager.queryIntentActivities(intent, 0)
    val packageNames = HashSet<String>()
    val appInfos = ArrayList<ApplicationInfo>()

    for (resolveInfo in resInfos) {
        packageNames.add(resolveInfo.activityInfo.packageName)
    }

    val mIgnore = db.ignoredPackages
    for (packageName in packageNames) {
        if (!mIgnore.contains(packageName)) {
            try {
                appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
            } catch (e: PackageManager.NameNotFoundException) {
                // Do Nothing
            }
        }
    }
    return appInfos
}

private fun applySort(
    packages: MutableList<ApplicationInfo>,
    sortOrder: SortOrder,
    packageManager: PackageManager,
    db: Db
) {
    when (sortOrder) {
        SortOrder.NAME -> {
            packages.sortWith(ApplicationInfo.DisplayNameComparator(packageManager))
        }
        SortOrder.CHECKED -> {
            packages.sortWith { o1, o2 ->
                val r1 = db.isAppEnabled(o1.packageName)
                val r2 = db.isAppEnabled(o2.packageName)
                var r = r2.compareTo(r1)
                if (r == 0) {
                    var s1 = packageManager.getApplicationLabel(o1)
                    if (s1 == null) {
                        s1 = o1.packageName
                    }
                    var s2 = packageManager.getApplicationLabel(o2)
                    if (s2 == null) {
                        s2 = o2.packageName
                    }
                    r = s1.toString().compareTo(s2.toString())
                }
                r
            }
        }
    }
}