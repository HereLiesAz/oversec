package io.oversec.one.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import io.oversec.one.R
import io.oversec.one.ui.viewModel.EncryptionParamsViewModel

@Composable
fun EncryptionParamsScreen(viewModel: EncryptionParamsViewModel = viewModel()) {
    val tabIndex = viewModel.tabIndex
    val tabs = listOf(
        stringResource(id = R.string.compose_tab__gpg_title),
        stringResource(id = R.string.compose_tab__sym_title),
        stringResource(id = R.string.compose_tab__simplesym_title)
    )

    Column {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { viewModel.onTabIndexChanged(index) },
                    text = { Text(text = title) }
                )
            }
        }
        when (tabIndex) {
            0 -> {
                // PGP Content
                Text("PGP Encryption Options")
            }
            1 -> {
                // Symmetric Content
                Text("Symmetric Encryption Options")
            }
            2 -> {
                // Simple Symmetric Content
                Text("Simple Symmetric Encryption Options")
            }
        }
    }
}
