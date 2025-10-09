package io.oversec.one.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kobakei.ratethisapp.RateThisApp
import io.oversec.one.Core
import io.oversec.one.Share
import io.oversec.one.Util
import io.oversec.one.crypto.Help
import io.oversec.one.db.Db
import io.oversec.one.iab.IabUtil
import io.oversec.one.ui.screen.AppConfigScreen
import io.oversec.one.ui.theme.OneTheme

class AppConfigActivity : AppCompatActivity() {

    private lateinit var core: Core
    private lateinit var db: Db
    private lateinit var packageNameExtra: String
    private var packageLabel: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        core = Core.getInstance(this)
        db = core.db
        packageNameExtra = intent.getStringExtra(EXTRA_PACKAGENAME) ?: "io.oversec.one"
        packageLabel = Util.getPackageLabel(this, packageNameExtra)

        if (IabUtil.isGooglePlayInstalled(this)) {
            RateThisApp.onCreate(this)
            RateThisApp.showRateDialogIfNeeded(this)
        }

        setContent {
            OneTheme {
                AppConfigScreen(
                    db = db,
                    packageName = packageNameExtra,
                    packageLabel = packageLabel.toString(),
                    isTempHidden = core.isTemporaryHidden(packageNameExtra),
                    onBackPressed = {
                        finish()
                        setupExitTransition()
                    },
                    onUpgrade = {
                        IabUtil.getInstance(this).showPurchaseActivity(this, RQ_UPGRADE)
                    },
                    onUnhide = {
                        core.undoTemporaryHide(packageNameExtra)
                        recreate() // To update the UI
                    },
                    onAppHelp = { pkgName ->
                        Help.INSTANCE.openForPackage(this, pkgName)
                    },
                    onBugReport = {
                        CrashActivity.sendBugReport(this, null) // General bug report
                    },
                    onShare = {
                        Share.share(this)
                    },
                    onAbout = {
                        AboutActivity.show(this)
                    },
                    onContextualHelp = { tabIndex ->
                        val anchor = when (tabIndex) {
                            0 -> Help.ANCHOR.appconfig_main
                            1 -> Help.ANCHOR.appconfig_appearance
                            2 -> Help.ANCHOR.appconfig_lab
                            3 -> Help.ANCHOR.main_padders
                            else -> null
                        }
                        Help.INSTANCE.open(this, anchor)
                    },
                    onAcsConfigureClick = {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    },
                    onBossKeyConfigureClick = {
                        core.setPanic(false)
                        recreate()
                    },
                    onOkcPlayStoreClick = {
                        Util.openPlayStore(this, "org.sufficientlysecure.keychain")
                    },
                    onOkcFdroidClick = {
                        Util.openUrl(this, "https://f-droid.org/packages/org.sufficientlysecure.keychain/")
                    },
                    numIgnoredTexts = core.numIgnoredTexts,
                    onClearIgnoredTexts = {
                        core.clearIgnoredTexts()
                        recreate()
                    }
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_UPGRADE) {
            if (resultCode == Activity.RESULT_OK) {
                recreate()
            }
        }
    }

    private fun setupExitTransition() {
        overridePendingTransition(0, R.anim.activity_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        ImeMemoryLeakWorkaroundDummyActivity.maybeShow(this)
    }

    companion object {
        const val EXTRA_PACKAGENAME = "packagename"
        const val RQ_UPGRADE = 9700
    }
}