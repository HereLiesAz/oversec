package io.oversec.one.ui.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import io.oversec.one.BuildConfig
import io.oversec.one.R
import io.oversec.one.iab.FullVersionListener
import io.oversec.one.iab.IabUtil
import io.oversec.one.iab.Purchase
import org.markdown4j.Markdown4jProcessor
import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter
import org.sufficientlysecure.htmltextview.HtmlTextView
import java.io.IOException
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackPressed: () -> Unit,
    isIabAvailable: Boolean
) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("About", "Changelog", if (isIabAvailable) "Purchases" else "Donations")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
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
                    Tab(text = { Text(title) },
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

@Composable
fun AboutTab() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier.padding(end = 10.dp)
            )
            Column {
                Text(text = "Oversec", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE}) ${if (BuildConfig.DEBUG) "DEBUG" else ""} [${BuildConfig.FLAVOR}]",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        AndroidView(
            factory = { context ->
                HtmlTextView(context)
            },
            update = { view ->
                try {
                    val html = Markdown4jProcessor().process(
                        context.resources.openRawResource(R.raw.about)
                    )
                    view.setHtml(html, HtmlAssetsImageGetter(context))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
fun ChangelogTab() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        AndroidView(
            factory = { context ->
                HtmlTextView(context)
            },
            update = { view ->
                try {
                    val html = Markdown4jProcessor().process(
                        context.resources.openRawResource(R.raw.changelog)
                    )
                    view.setHtml(html, HtmlAssetsImageGetter(context))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        )
    }
}

@Composable
fun PurchasesTab() {
    val context = LocalContext.current
    var purchases by remember { mutableStateOf<List<Purchase>>(emptyList()) }
    val iabUtil = IabUtil.getInstance(context)

    LaunchedEffect(Unit) {
        iabUtil.checkFullVersionAndLoadSkuDetails(object : FullVersionListener {
            override fun onFullVersion_MAIN_THREAD(isFullVersion: Boolean) {
                iabUtil.inventory?.let { inventory ->
                    purchases = inventory.allPurchases.filter { it.purchaseState == 0 }
                }
            }
        })
    }

    LazyColumn {
        items(purchases) { purchase ->
            PurchaseItem(purchase)
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
    val text = "$date   $title"

    Text(text = text, modifier = Modifier.padding(16.dp))
}

@Composable
fun DonationsTab() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = stringResource(id = R.string.donate_body),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DonationButton(coinResId = R.string.donate_btn_btc, address = BuildConfig.DONATION_BTC)
        DonationButton(coinResId = R.string.donate_btn_eth, address = BuildConfig.DONATION_ETH)
        DonationButton(coinResId = R.string.donate_btn_iota, address = BuildConfig.DONATION_IOTA)
        DonationButton(coinResId = R.string.donate_btn_dash, address = BuildConfig.DONATION_DASH)
    }
}

@Composable
fun DonationButton(coinResId: Int, address: String) {
    val context = LocalContext.current
    val coin = stringResource(id = coinResId)
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 18.sp)) {
            append(coin)
        }
        append("\n")
        withStyle(style = SpanStyle(fontSize = 12.sp)) {
            append(address)
        }
    }

    Button(
        onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(context.getString(R.string.donate_clip_label), address)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, R.string.donate_toast_copied, Toast.LENGTH_LONG).show()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(text = annotatedString)
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen(onBackPressed = {}, isIabAvailable = true)
}