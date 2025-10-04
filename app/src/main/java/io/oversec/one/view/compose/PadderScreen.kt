package io.oversec.one.view.compose

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import io.oversec.one.R
import io.oversec.one.common.CoreContract
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.crypto.encoding.pad.XCoderAndPadderFactory
import io.oversec.one.db.PadderDb
import io.oversec.one.view.AppConfigActivity
import io.oversec.one.view.PadderDetailActivity

private const val RQ_UPGRADE = 8010

@Composable
fun PadderScreen() {
    val context = LocalContext.current
    val db = remember { PadderDb.getInstance(context) }
    var padders by remember { mutableStateOf(db.allValues) }

    val padderDetailLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        padders = db.allValues
        XCoderAndPadderFactory.getInstance(context).reload()
    }

    val upgradeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            showAddNewPadder(context, padderDetailLauncher)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(padders) { padder ->
                PadderListItem(padder = padder) {
                    val intent = Intent(context, PadderDetailActivity::class.java).apply {
                        putExtra(PadderDetailActivity.EXTRA_KEY, padder.key)
                    }
                    padderDetailLauncher.launch(intent)
                }
            }
        }
        FloatingActionButton(
            onClick = {
                showAddNewPadderIfFullVersion(context, padderDetailLauncher, upgradeLauncher)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_black_24dp),
                contentDescription = "Add Padder"
            )
        }
    }
}

@Composable
fun PadderListItem(padder: PadderContent, onClick: () -> Unit) {
    Column(modifier = Modifier.clickable { onClick() }) {
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = padder.name,
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = padder.contentBegin,
                modifier = Modifier
                    .weight(3f)
                    .padding(start = 10.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Divider()
    }
}

private fun showAddNewPadderIfFullVersion(
    context: Context,
    padderLauncher: androidx.activity.result.ActivityResultLauncher<Intent>,
    upgradeLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    CoreContract.getInstance().doIfFullVersionOrShowPurchaseDialog(
        context as Activity,
        {
            showAddNewPadder(context, padderLauncher)
        },
        RQ_UPGRADE
    )
}

private fun showAddNewPadder(
    context: Context,
    launcher: androidx.activity.result.ActivityResultLauncher<Intent>
) {
    val intent = Intent(context, PadderDetailActivity::class.java)
    launcher.launch(intent)
}