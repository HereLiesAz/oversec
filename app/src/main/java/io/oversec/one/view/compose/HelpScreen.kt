package io.oversec.one.view.compose

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.oversec.one.BuildConfig
import io.oversec.one.Core
import io.oversec.one.CrashActivity
import io.oversec.one.R
import io.oversec.one.Share
import io.oversec.one.acs.util.AndroidIntegration
import io.oversec.one.crypto.AppsReceiver
import io.oversec.one.crypto.Help
import io.oversec.one.crypto.LoggingConfig
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.iab.FullVersionListener
import io.oversec.one.iab.IabUtil
import io.oversec.one.view.ActionAccessibilitySettingsNotResolvableActivity
import io.oversec.one.view.OnboardingActivity
import io.oversec.one.view.PostAcsEnableActivity
import io.oversec.one.view.util.GotItPreferences
import java.util.*

@Composable
fun HelpScreen() {
    val context = LocalContext.current
    val activity = context as Activity
    val core = remember { Core.getInstance(context) }
    val iabUtil = remember { IabUtil.getInstance(context) }
    val okcConnector = remember { OpenKeychainConnector.getInstance(context) }

    var accessibilitySettingsOn by remember { mutableStateOf(AndroidIntegration.isAccessibilitySettingsOnAndServiceRunning(context)) }
    var isBossMode by remember { mutableStateOf(core.db.isBossMode) }
    var isFullVersion by remember { mutableStateOf(true) } // Assume true until checked
    var okcVersion by remember { mutableStateOf(okcConnector.version) }
    var okcVersionName by remember { mutableStateOf(okcConnector.versionName) }
    val isGooglePlayInstalled by remember { mutableStateOf(okcConnector.isGooglePlayInstalled()) }

    val accessibilitySettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        // After returning from settings, update the status
        accessibilitySettingsOn = AndroidIntegration.isAccessibilitySettingsOnAndServiceRunning(context)
    }

    // This effect will run when accessibilitySettingsOn changes to true
    LaunchedEffect(accessibilitySettingsOn) {
        if (accessibilitySettingsOn) {
            PostAcsEnableActivity.show(context)
        }
    }

    DisposableEffect(core.observableCore) {
        val observer = Observer { _, _ ->
            accessibilitySettingsOn = AndroidIntegration.isAccessibilitySettingsOnAndServiceRunning(context)
            isBossMode = core.db.isBossMode
        }
        core.observableCore.addObserver(observer)
        onDispose { core.observableCore.deleteObserver(observer) }
    }

    DisposableEffect(iabUtil) {
        val listener = FullVersionListener { isFull -> isFullVersion = isFull }
        iabUtil.addListener(listener)
        if (iabUtil.isIabAvailable) {
            iabUtil.checkFullVersion(listener)
        }
        onDispose { iabUtil.removeListener(listener) }
    }

    DisposableEffect(Unit) {
        val listener = object : AppsReceiver.IAppsReceiverListener {
            override fun onAppChanged(ctx: Context, action: String, packagename: String) {
                okcVersion = okcConnector.version
                okcVersionName = okcConnector.versionName
            }
        }
        AppsReceiver.addListener(listener)
        onDispose { AppsReceiver.removeListener(listener) }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (BuildConfig.DEBUG) {
            item { DebugButtons() }
        }
        item { AcsStatus(accessibilitySettingsOn, accessibilitySettingsLauncher) }
        if (isBossMode) {
            item { BossStatus() }
        }
        item { ActionButtons() }
        if (!isFullVersion) {
            item { UpgradeSection() }
        }
        if (io.oversec.one.Util.isFeatureEnctypePGP(context) && okcVersion < OpenKeychainConnector.V_MIN) {
            item { OkcSection(okcVersion, okcVersionName, isGooglePlayInstalled) }
        }
    }
}

@Composable
private fun DebugButtons() {
    val context = LocalContext.current
    val activity = context as Activity
    val iabUtil = remember { IabUtil.getInstance(context) }
    Column {
        Button(onClick = { LoggingConfig.setLog(true) }) {
            Text(text = "LOGGING ON")
        }
        Button(onClick = { LoggingConfig.setLog(false) }) {
            Text(text = "LOGGING OFF")
        }
        Button(onClick = {
            iabUtil.consumeAll {
                activity.runOnUiThread {
                    // Re-check full version status after consuming
                    iabUtil.checkFullVersion { }
                }
            }
        }) {
            Text(text = "CONSUME ALL")
        }
    }
}

@Composable
private fun AcsStatus(
    enabled: Boolean,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    val context = LocalContext.current
    if (enabled) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.settings_acs_enabled_ok),
                modifier = Modifier.padding(16.dp)
            )
        }
    } else {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = stringResource(id = R.string.settings_acs_not_enabled))
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { Help.open(context, Help.ANCHOR.main_help_acsconfig) }) {
                        Text(text = stringResource(id = R.string.action_moreinfo))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    val infiniteTransition = rememberInfiniteTransition()
                    val color by infiniteTransition.animateColor(
                        initialValue = Color.White,
                        targetValue = Color.Black,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )
                    Button(
                        onClick = { openAcsSettings(context, false, launcher) },
                        colors = ButtonDefaults.buttonColors(contentColor = color)
                    ) {
                        Text(text = stringResource(id = R.string.action_configure))
                    }
                }
            }
        }
    }
}

@Composable
private fun BossStatus() {
    val context = LocalContext.current
    val core = remember { Core.getInstance(context) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.settings_boss_active))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { Help.open(context, Help.ANCHOR.bossmode_active) }) {
                    Text(text = stringResource(id = R.string.action_moreinfo))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { core.disablePanicMode() }) {
                    Text(text = stringResource(id = R.string.action_reenable_boss))
                }
            }
        }
    }
}

@Composable
private fun ActionButtons() {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { Help.open(context) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.settings_help))
        }
        Button(onClick = { CrashActivity.sendBugReport(context, null) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.action_send_bugreport))
        }
        Button(onClick = { Share.share(context) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.action_share_app))
        }
    }
}

@Composable
private fun UpgradeSection() {
    val context = LocalContext.current
    val activity = context as Activity
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(id = R.string.settings_upgrade))
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { IabUtil.getInstance(activity).showPurchaseActivity(activity) }) {
                    Text(text = stringResource(id = R.string.action_upgrade))
                }
            }
        }
    }
}

@Composable
private fun OkcSection(okcVersion: Int, okcVersionName: String?, isGooglePlayInstalled: Boolean) {
    val context = LocalContext.current
    val okcConnector = remember { OpenKeychainConnector.getInstance(context) }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            val statusText = if (okcVersion == -1) {
                stringResource(id = io.oversec.one.crypto.R.string.settings_okc_not_installed)
            } else {
                stringResource(id = io.oversec.one.crypto.R.string.okc_installed_but_too_old, okcVersionName)
            }
            Text(text = statusText)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { Help.open(context, Help.ANCHOR.encparams_pgp) }) {
                    Text(text = stringResource(id = R.string.action_moreinfo))
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (isGooglePlayInstalled) {
                    Button(onClick = { okcConnector.openInPlayStore() }) {
                        Text(text = stringResource(id = R.string.okc_install_googleplay))
                    }
                }
                if (BuildConfig.IS_FRDOID) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { okcConnector.openInFdroid() }) {
                        Text(text = stringResource(id = R.string.okc_install_fdroid))
                    }
                }
            }
        }
    }
}

private fun openAcsSettings(
    context: Context,
    forceShowOnboarding: Boolean,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    val packageManager: PackageManager = context.packageManager
    if (intent.resolveActivity(packageManager) != null) {
        launcher.launch(intent)
        if (!GotItPreferences.getPreferences(context)
                .isTooltipConfirmed(context.getString(R.string.tooltipid_onboarding))
            || forceShowOnboarding
        ) {
            OnboardingActivity.show(context)
        }
    } else {
        ActionAccessibilitySettingsNotResolvableActivity.show(context)
    }
}