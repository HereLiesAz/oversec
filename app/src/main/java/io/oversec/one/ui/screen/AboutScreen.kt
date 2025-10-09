package io.oversec.one.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.oversec.one.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    isIabAvailable: Boolean,
    onBackPressed: () -> Unit
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.about_tab_about),
        stringResource(R.string.about_tab_changelog),
        stringResource(if (isIabAvailable) R.string.about_tab_purchases else R.string.about_tab_donations)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_activity_about)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
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
                0 -> AboutTab()
                1 -> ChangelogTab()
                2 -> if (isIabAvailable) PurchasesTab() else DonationsTab()
            }
        }
    }
}

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.oversec.one.BuildConfig
import io.oversec.one.iab.FullVersionListener
import io.oversec.one.iab.IabUtil
import io.oversec.one.iab.Purchase
import java.util.Date

@Composable
fun AboutTab() {
    val context = LocalContext.current
    val markdown = remember {
        context.resources.openRawResource(R.raw.about).bufferedReader().use { it.readText() }
    }
    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) " +
                            (if (BuildConfig.DEBUG) " DEBUG" else "") + " [${BuildConfig.FLAVOR}]",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = markdown)
    }
}

@Composable
fun ChangelogTab() {
    val context = LocalContext.current
    val markdown = remember {
        context.resources.openRawResource(R.raw.changelog).bufferedReader().use { it.readText() }
    }
    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(text = markdown)
    }
}

@Composable
fun PurchasesTab() {
    val context = LocalContext.current
    var purchases by remember { mutableStateOf<List<Purchase>>(emptyList()) }

    LaunchedEffect(Unit) {
        val iabUtil = IabUtil.getInstance(context)
        iabUtil.checkFullVersionAndLoadSkuDetails(object : FullVersionListener {
            override fun onFullVersion_MAIN_THREAD(isFullVersion: Boolean) {
                iabUtil.inventory?.let { inv ->
                    purchases = inv.allPurchases.filter { it.purchaseState == 0 }
                }
            }
        })
    }

    if (purchases.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No purchases found.")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(purchases) { purchase ->
                PurchaseItem(purchase = purchase)
            }
        }
    }
}

@Composable
fun PurchaseItem(purchase: Purchase) {
    val context = LocalContext.current
    val iabUtil = IabUtil.getInstance(context)
    val skuDetails = iabUtil.inventory?.getSkuDetails(purchase.sku)

    val title = skuDetails?.title ?: purchase.sku
    val date = DateFormat.getMediumDateFormat(context).format(Date(purchase.purchaseTime))

    Text(
        text = "$date   $title",
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun DonationsTab() {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(
            text = stringResource(id = R.string.donate_body),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        DonationButton(
            coinName = "Bitcoin",
            address = BuildConfig.DONATION_BTC,
            clipboardManager = clipboardManager,
            context = context
        )
        DonationButton(
            coinName = "Ethereum",
            address = BuildConfig.DONATION_ETH,
            clipboardManager = clipboardManager,
            context = context
        )
        DonationButton(
            coinName = "IOTA",
            address = BuildConfig.DONATION_IOTA,
            clipboardManager = clipboardManager,
            context = context
        )
        DonationButton(
            coinName = "Dash",
            address = BuildConfig.DONATION_DASH,
            clipboardManager = clipboardManager,
            context = context
        )
    }
}

@Composable
fun DonationButton(coinName: String, address: String, clipboardManager: ClipboardManager, context: Context) {
    if (address.isNotBlank()) {
        Button(
            onClick = {
                val clip = ClipData.newPlainText(context.getString(R.string.donate_clip_label), address)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(context, R.string.donate_toast_copied, Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = coinName, style = MaterialTheme.typography.titleMedium)
                Text(text = address, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}