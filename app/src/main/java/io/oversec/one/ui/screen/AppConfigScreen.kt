package io.oversec.one.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.oversec.one.R
import io.oversec.one.db.Db

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppConfigScreen(
    db: Db,
    packageName: String,
    packageLabel: String,
    isTempHidden: Boolean,
    onBackPressed: () -> Unit,
    onUpgrade: () -> Unit,
    onUnhide: () -> Unit,
    onAppHelp: (String) -> Unit,
    onBugReport: () -> Unit,
    onShare: () -> Unit,
    onAbout: () -> Unit,
    onContextualHelp: (Int) -> Unit,
    onAcsConfigureClick: () -> Unit,
    onBossKeyConfigureClick: () -> Unit,
    onOkcPlayStoreClick: () -> Unit,
    onOkcFdroidClick: () -> Unit,
    numIgnoredTexts: Int,
    onClearIgnoredTexts: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.tab_stuff),
        stringResource(R.string.tab_colors),
        stringResource(R.string.tab_expert),
        stringResource(R.string.tab_padders)
    )
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(packageLabel) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(R.string.back_content_description))
                    }
                },
                actions = {
                    IconButton(onClick = { onContextualHelp(tabIndex) }) {
                        Icon(Icons.Filled.HelpOutline, contentDescription = stringResource(R.string.action_help))
                    }
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.more_options_content_description))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.action_share_app)) },
                            onClick = {
                                onShare()
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.about)) },
                            onClick = {
                                onAbout()
                                showMenu = false
                            }
                        )
                        if (numIgnoredTexts > 0) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.action_clear_ignored_keys, numIgnoredTexts)) },
                                onClick = {
                                    onClearIgnoredTexts()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isTempHidden) {
                Button(onClick = onUnhide) {
                    Text(stringResource(R.string.undo_temporary_hide))
                }
            }
            TabRow(selectedTabIndex = tabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index }
                    )
                }
            }
            when (tabIndex) {
                0 -> MainSettingsScreen(
                    db = db,
                    onAppHelpClick = onAppHelp,
                    onHelpClick = { onContextualHelp(0) },
                    onBugReportClick = onBugReport,
                    onShareClick = onShare,
                    onUpgradeClick = onUpgrade,
                    onAcsConfigureClick = onAcsConfigureClick,
                    onBossKeyConfigureClick = onBossKeyConfigureClick,
                    onOkcPlayStoreClick = onOkcPlayStoreClick,
                    onOkcFdroidClick = onOkcFdroidClick
                )
                1 -> ColorsTweakScreen(db = db, packageName = packageName, onUpgrade = onUpgrade)
                2 -> ExpertTweaksScreen(db = db, packageName = packageName)
                3 -> PaddersScreen(db = db)
            }
        }
    }
}