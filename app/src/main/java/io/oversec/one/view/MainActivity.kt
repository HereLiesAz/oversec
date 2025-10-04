package io.oversec.one.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.pagerTabIndicatorOffset
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.Share
import io.oversec.one.Util
import io.oversec.one.crypto.Help
import io.oversec.one.view.compose.AppsScreen
import io.oversec.one.view.compose.HelpScreen
import io.oversec.one.view.compose.KeysScreen
import io.oversec.one.view.compose.PadderScreen
import io.oversec.one.view.compose.SettingsScreen
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TAB = "tab"
        const val TAB_HELP = "help"
        const val TAB_APPS = "apps"
        const val TAB_KEYS = "keys"
        const val TAB_SETTINGS = "settings"
        const val TAB_PADDER = "padder"
        const val EXTRA_CONFIRM_DIALERCODE_BROADCAST_WORKING = "EXTRA_CONFIRM_DIALERCODE_BROADCAST_WORKING"
        private var mInstance: MainActivity? = null

        fun show(ctx: Context) {
            val i = Intent()
            i.setClass(ctx, MainActivity::class.java)
            i.putExtra(EXTRA_TAB, TAB_HELP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(i)
        }

        fun showApps(ctx: Context) {
            val i = Intent()
            i.setClass(ctx, MainActivity::class.java)
            i.putExtra(EXTRA_TAB, TAB_APPS)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            ctx.startActivity(i)
        }

        fun showKeys(ctx: Context) {
            val i = Intent()
            i.setClass(ctx, MainActivity::class.java)
            i.putExtra(EXTRA_TAB, TAB_KEYS)
            ctx.startActivity(i)
        }

        fun showSettings(ctx: Context) {
            val i = Intent()
            i.setClass(ctx, MainActivity::class.java)
            i.putExtra(EXTRA_TAB, TAB_SETTINGS)
            ctx.startActivity(i)
        }

        fun confirmDialerSecretCodeBroadcastWorking(ctx: Context) {
            val i = Intent()
            i.setClass(ctx, MainActivity::class.java)
            i.putExtra(EXTRA_TAB, TAB_SETTINGS)
            i.putExtra(EXTRA_CONFIRM_DIALERCODE_BROADCAST_WORKING, true)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            ctx.startActivity(i)
        }

        fun closeOnPanic() {
            mInstance?.finish()
        }
    }

    private val tabs = mutableListOf<String>()

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mInstance = this

        tabs.add(TAB_HELP)
        if (Util.isOversec(this)) {
            tabs.add(TAB_APPS)
        }
        if (Util.isFeatureEnctypeSYM(this)) {
            tabs.add(TAB_KEYS)
        }
        tabs.add(TAB_SETTINGS)
        tabs.add(TAB_PADDER)

        setContent {
            MainScreen(tabs = tabs, intent = intent)
        }
    }

    override fun onDestroy() {
        mInstance = null
        super.onDestroy()
        ImeMemoryLeakWorkaroundDummyActivity.maybeShow(this)
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(tabs: List<String>, intent: Intent) {
    Column {
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                // TODO: Add menu items here
            }
        )
        if (tabs.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { tabs.size })
            val coroutineScope = rememberCoroutineScope()

            val initialTab = intent.getStringExtra(MainActivity.EXTRA_TAB)
            if (initialTab != null) {
                val index = tabs.indexOf(initialTab)
                if (index != -1) {
                    LaunchedEffect(pagerState) {
                        pagerState.scrollToPage(index)
                    }
                }
            }

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    if (pagerState.currentPage < tabPositions.size) {
                        TabRowDefaults.Indicator(
                            Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                        )
                    }
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = getTabTitle(title)) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (tabs[page]) {
                    MainActivity.TAB_HELP -> HelpScreen()
                    MainActivity.TAB_APPS -> AppsScreen()
                    MainActivity.TAB_KEYS -> KeysScreen()
                    MainActivity.TAB_SETTINGS -> SettingsScreen()
                    MainActivity.TAB_PADDER -> PadderScreen()
                }
            }
        }
    }
}

@Composable
fun getTabTitle(tab: String): String {
    return when (tab) {
        MainActivity.TAB_APPS -> stringResource(R.string.main_tab_apps)
        MainActivity.TAB_HELP -> stringResource(R.string.main_tab_help)
        MainActivity.TAB_KEYS -> stringResource(R.string.main_tab_keys)
        MainActivity.TAB_PADDER -> stringResource(R.string.main_tab_padder)
        MainActivity.TAB_SETTINGS -> stringResource(R.string.main_tab_settings)
        else -> ""
    }
}