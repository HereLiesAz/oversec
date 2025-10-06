package io.oversec.one.ui.screen.main

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.oversec.one.*
import io.oversec.one.acs.util.AndroidIntegration
import io.oversec.one.crypto.Help
import io.oversec.one.crypto.LoggingConfig
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.iab.FullVersionListener
import io.oversec.one.iab.IabUtil
import io.oversec.one.view.ActionAccessibilitySettingsNotResolvableActivity
import io.oversec.one.view.OnboardingActivity
import io.oversec.one.view.util.GotItPreferences
import java.util.*

@Composable
fun HelpScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State variables
    var isAcsEnabled by remember { mutableStateOf(AndroidIntegration.isAccessibilitySettingsOnAndServiceRunning(context)) }
    var isBossMode by remember { mutableStateOf(Core.getInstance(context).db.isBossMode) }
    var isFullVersion by remember { mutableStateOf(false) }
    var okcStatus by remember { mutableStateOf(OkcStatus.LOADING) }
    var okcVersionName by remember { mutableStateOf("") }

    // Observers and Listeners
    DisposableEffect(context) {
        val core = Core.getInstance(context)
        val iabUtil = IabUtil.getInstance(context)

        val accessibilityObserver = Observer { _, _ ->
            isAcsEnabled = AndroidIntegration.isAccessibilitySettingsOnAndServiceRunning(context)
        }
        core.observableCore.addObserver(accessibilityObserver)

        val fullVersionListener = FullVersionListener { isFull ->
            isFullVersion = isFull
        }
        iabUtil.addListener(fullVersionListener)
        if (iabUtil.isIabAvailable) {
            iabUtil.checkFullVersion(fullVersionListener)
        }

        // Check OKC status
        val okcConnector = OpenKeychainConnector.getInstance(context)
        val version = okcConnector.version
        if (!Util.isFeatureEnctypePGP(context)) {
            okcStatus = OkcStatus.NOT_NEEDED
        } else if (version >= OpenKeychainConnector.V_MIN) {
            okcStatus = OkcStatus.OK
        } else if (version == -1) {
            okcStatus = OkcStatus.NOT_INSTALLED
        } else {
            okcStatus = OkcStatus.TOO_OLD
            okcVersionName = okcConnector.versionName
        }

        onDispose {
            core.observableCore.deleteObserver(accessibilityObserver)
            iabUtil.removeListener(fullVersionListener)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (BuildConfig.DEBUG) {
            DebugSection()
        }

        AccessibilityServiceSection(isEnabled = isAcsEnabled)

        if (isBossMode) {
            BossModeSection()
        }

        MainActionsSection()

        if (IabUtil.getInstance(context).isIabAvailable && !isFullVersion) {
            UpgradeSection()
        }

        if (okcStatus != OkcStatus.NOT_NEEDED && okcStatus != OkcStatus.OK) {
            OpenKeychainSection(status = okcStatus, versionName = okcVersionName)
        }
    }
}

@Composable
private fun DebugSection() {
    val context = LocalContext.current
    InfoCard {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { LoggingConfig.INSTANCE.log = true }) { Text("LOG ON") }
            Button(onClick = { LoggingConfig.INSTANCE.log = false }) { Text("LOG OFF") }
            Button(onClick = {
                IabUtil.getInstance(context).consumeAll {}
            }) { Text("CONSUME ALL") }
        }
    }
}

@Composable
private fun AccessibilityServiceSection(isEnabled: Boolean) {
    val context = LocalContext.current
    if (isEnabled) {
        InfoCard(isTransparent = true) {
            Text(
                text = stringResource(R.string.settings_acs_enabled_ok),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(6.dp)
            )
        }
    } else {
        InfoCard {
            Text(
                text = stringResource(R.string.settings_acs_not_enabled),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(6.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { Help.INSTANCE.open(context, Help.ANCHOR.main_help_acsconfig) }) {
                    Text(stringResource(R.string.action_moreinfo))
                }
                FlashingButton(
                    onClick = { openAcsSettings(context) },
                    text = stringResource(R.string.action_configure)
                )
            }
        }
    }
}

@Composable
private fun BossModeSection() {
    val context = LocalContext.current
    InfoCard {
        Text(
            text = stringResource(R.string.settings_boss_active),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(6.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { Help.INSTANCE.open(context, Help.ANCHOR.bossmode_active) }) {
                Text(stringResource(R.string.action_moreinfo))
            }
            Button(onClick = { Core.getInstance(context).disablePanicMode() }) {
                Text(stringResource(R.string.action_reenable_boss))
            }
        }
    }
}

@Composable
private fun MainActionsSection() {
    val context = LocalContext.current
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Button(onClick = { Help.INSTANCE.open(context) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_help))
        }
        Button(onClick = { CrashActivity.sendBugReport(context, null) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_send_bugreport))
        }
        Button(onClick = { Share.share(context) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.action_share_app))
        }
    }
}

@Composable
private fun UpgradeSection() {
    val context = LocalContext.current
    InfoCard(isTransparent = true) {
        Text(
            text = stringResource(R.string.settings_upgrade),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(6.dp)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = { IabUtil.getInstance(context).showPurchaseActivity(context) }) {
                Text(stringResource(R.string.action_upgrade))
            }
        }
    }
}

private enum class OkcStatus {
    LOADING, NOT_NEEDED, OK, NOT_INSTALLED, TOO_OLD
}

@Composable
private fun OpenKeychainSection(status: OkcStatus, versionName: String) {
    val context = LocalContext.current
    val okcConnector = OpenKeychainConnector.getInstance(context)

    InfoCard(isTransparent = true) {
        val statusText = when (status) {
            OkcStatus.NOT_INSTALLED -> stringResource(io.oversec.one.crypto.R.string.settings_okc_not_installed)
            OkcStatus.TOO_OLD -> stringResource(io.oversec.one.crypto.R.string.okc_installed_but_too_old, versionName)
            else -> ""
        }
        Text(text = statusText, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(6.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { Help.INSTANCE.open(context, Help.ANCHOR.encparams_pgp) }) {
                Text(stringResource(R.string.action_moreinfo))
            }
            if (BuildConfig.IS_FRDOID) {
                Button(onClick = { okcConnector.openInFdroid() }) {
                    Text(stringResource(R.string.okc_install_fdroid))
                }
            }
            if (okcConnector.isGooglePlayInstalled) {
                 Button(onClick = { okcConnector.openInPlayStore() }) {
                    Text(stringResource(R.string.okc_install_googleplay))
                }
            }
        }
    }
}

@Composable
private fun FlashingButton(onClick: () -> Unit, text: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "flashingButton")
    val color by infiniteTransition.animateColor(
        initialValue = Color.White,
        targetValue = Color.Black,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "flashingButtonColor"
    )

    Button(onClick = onClick) {
        Text(text, color = color)
    }
}

@Composable
private fun InfoCard(isTransparent: Boolean = false, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if(isTransparent) CardDefaults.cardColors(containerColor = Color.Transparent) else CardDefaults.cardColors(),
        elevation = if(isTransparent) CardDefaults.cardElevation(defaultElevation = 0.dp) else CardDefaults.cardElevation(),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            content()
        }
    }
}

private fun openAcsSettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
        if (!GotItPreferences.getPreferences(context).isTooltipConfirmed(context.getString(R.string.tooltipid_onboarding))) {
            OnboardingActivity.show(context)
        }
    } else {
        ActionAccessibilitySettingsNotResolvableActivity.show(context)
    }
}