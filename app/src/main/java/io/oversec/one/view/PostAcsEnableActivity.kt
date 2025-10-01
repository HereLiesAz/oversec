package io.oversec.one.view

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.Util
import io.oversec.one.db.Db

class PostAcsEnableActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TARGET_APP_NOT_INSTALLED = "tani"

        fun show(ctx: Context) {
            var targetAppStarted = false
            var targetAppNotInstalled = false

            Core.getInstance(ctx).setInitiallyDisabled(true)

            if (!Util.isOversec(ctx)) {
                //start target app, but do NOT yet process it, otherwise we'll get oversec overlays on top of this app

                val packagename = ctx.resources.getString(R.string.feature_package)
                val pm = ctx.packageManager
                val intent = pm.getLaunchIntentForPackage(packagename)
                if (intent != null) {
                    targetAppStarted = true
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    ctx.startActivity(intent)
                } else {
                    targetAppNotInstalled = true
                }
            }

            val i = Intent()
            i.setClass(ctx, PostAcsEnableActivity::class.java)
            if (targetAppStarted) {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (targetAppNotInstalled) {
                i.putExtra(EXTRA_TARGET_APP_NOT_INSTALLED, true)
            }
            ctx.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val targetAppNotInstalled = intent.getBooleanExtra(EXTRA_TARGET_APP_NOT_INSTALLED, false)
        val isOversec = Util.isOversec(this)
        val enabledPackages = getEnabledPackages()

        setContent {
            PostAcsEnableScreen(
                isOversec = isOversec,
                targetAppNotInstalled = targetAppNotInstalled,
                enabledPackages = enabledPackages,
                onConfigureAppsClick = { goBackToMainApp() },
                onAppClick = { packageName ->
                    finish()
                    MainActivity.showApps(this@PostAcsEnableActivity) //this will clear the stack with the settings activities

                    val pm = packageManager
                    val intent = pm.getLaunchIntentForPackage(packageName)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                },
                onOkClick = { finish() }
            )
        }
    }

    private fun goBackToMainApp() {
        finish()
        MainActivity.showApps(this)
    }

    override fun onStop() {
        //signal the ACS that'we ready to go
        Core.getInstance(this).setInitiallyDisabled(false)
        super.onStop()
    }

    override fun onBackPressed() {
        finish() //-> this should bring us back to the Main activity
    }

    private fun getEnabledPackages(): List<ApplicationInfo> {
        val db = Core.getInstance(this).db

        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resInfos = packageManager.queryIntentActivities(intent, 0)
        //using hashset so that there will be no duplicate packages,
        //if no duplicate packages then there will be no duplicate apps
        val packageNames = HashSet<String>(0)
        val appInfos = ArrayList<ApplicationInfo>(0)

        //getting package names and adding them to the hashset
        for (resolveInfo in resInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName)
        }

        //now we have unique packages in the hashset, so get their application infos
        //and add them to the arraylist

        for (packageName in packageNames) {

            if (db.isAppEnabled(packageName)) {
                try {
                    appInfos.add(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA))
                } catch (e: PackageManager.NameNotFoundException) {
                    //Do Nothing
                }
            }
        }

        //to sort the list of apps by their names
        appInfos.sortWith(ApplicationInfo.DisplayNameComparator(packageManager))
        return appInfos
    }
}

@Composable
fun PostAcsEnableScreen(
    isOversec: Boolean,
    targetAppNotInstalled: Boolean,
    enabledPackages: List<ApplicationInfo>,
    onConfigureAppsClick: () -> Unit,
    onAppClick: (String) -> Unit,
    onOkClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isOversec) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(enabledPackages) { appInfo ->
                    AppListItem(appInfo = appInfo, onClick = { onAppClick(appInfo.packageName) })
                    Divider()
                }
            }
            Button(onClick = onConfigureAppsClick) {
                Text(text = stringResource(id = R.string.configure_apps))
            }
        } else {
            val text = if (targetAppNotInstalled) {
                stringResource(id = R.string.settings_acs_post_enable_msg_appsec_target_package_not_installed, stringResource(id = R.string.feature_package))
            } else {
                stringResource(id = R.string.settings_acs_post_enable_msg_appsec, Util.getPackageLabel(context = androidx.compose.ui.platform.LocalContext.current, packageName = stringResource(id = R.string.feature_package)))
            }
            Text(text = text)
            Button(onClick = onOkClick) {
                Text(text = stringResource(id = R.string.action_ok))
            }
        }
    }
}

@Composable
fun AppListItem(appInfo: ApplicationInfo, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(data = appInfo.loadIcon(androidx.compose.ui.platform.LocalContext.current.packageManager)),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = appInfo.loadLabel(androidx.compose.ui.platform.LocalContext.current.packageManager).toString())
    }
}
