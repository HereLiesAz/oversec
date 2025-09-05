package io.oversec.one.view

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.oversec.one.R
import io.oversec.one.common.MainPreferences
import io.oversec.one.crypto.TemporaryContentProvider
import io.oversec.one.crypto.sym.*
import io.oversec.one.crypto.symbase.KeyUtil
import io.oversec.one.crypto.symbase.OversecKeyCacheListener
import io.oversec.one.ui.SecureBaseActivity
import java.text.SimpleDateFormat
import java.util.*

class KeyDetailsActivity : SecureBaseActivity(), OversecKeyCacheListener {

    private lateinit var mKeystore: OversecKeystore2
    private var mId: Long = 0
    private val showErrorDialog = mutableStateOf(false)
    private val errorMessage = mutableStateOf("")

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mKeystore = OversecKeystore2.getInstance(this)

        if (!MainPreferences.isAllowScreenshots(this)) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        mId = intent.getLongExtra(EXTRA_ID, 0)
        val key = mKeystore.getSymmetricKeyEncrypted(mId)
        if (key == null) {
            Log.w("KeyDetailsActivity", "couldn't find request key with id $mId")
            finish()
            return
        }

        setContent {
            val showError by showErrorDialog
            val errorMsg by errorMessage

            MaterialTheme {
                KeyDetailsScreen(
                    key = key,
                    onNavigateUp = { finish() },
                    onConfirmKey = {
                        try {
                            mKeystore.confirmKey(mId)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showError(getString(R.string.common_error_body, e.message))
                        }
                    },
                    onDeleteKey = {
                        try {
                            mKeystore.deleteKey(mId)
                            setResult(Activity.RESULT_FIRST_USER)
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showError(getString(R.string.common_error_body, e.message))
                        }
                    },
                    getQrBitmap = { isBlurred ->
                        getQrBitmap(isBlurred)
                    },
                    onUnlockKey = { launcher ->
                        try {
                            val intent = Intent(this, UnlockKeyActivity::class.java)
                            intent.putExtra("id", mId)
                            launcher.launch(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    },
                    showErrorDialog = showError,
                    errorMessage = errorMsg,
                    onDismissErrorDialog = { showErrorDialog.value = false }
                )
            }
        }
        mKeystore.addKeyCacheListener(this)
    }

    private fun showError(message: String) {
        errorMessage.value = message
        showErrorDialog.value = true
    }

    private fun getQrBitmap(blur: Boolean): Bitmap? {
        val dimension =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240f, resources.displayMetrics)
                .toInt()
        return try {
            if (blur) {
                val bm = SymUtil.getQrCode(KeyUtil.getRandomBytes(32), dimension)
                val bmSmallTmp = Bitmap.createScaledBitmap(bm!!, 25, 25, true)
                Bitmap.createScaledBitmap(bmSmallTmp, dimension, dimension, true)
            } else {
                SymUtil.getQrCode(mKeystore.getPlainKeyAsTransferBytes(mId), dimension)
            }
        } catch (ex: KeyNotCachedException) {
            null // Return null to indicate key needs unlocking
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    override fun onDestroy() {
        mKeystore.removeKeyCacheListener(this)
        super.onDestroy()
    }

    override fun onFinishedCachingKey(keyId: Long) {
        if (mId == keyId) {
            finish()
        }
    }
    override fun onStartedCachingKey(keyId: Long) {}

    companion object {
        private const val EXTRA_ID = "id"
        fun show(ctx: Context, keyId: Long?) {
            val i = Intent(ctx, KeyDetailsActivity::class.java)
            if (ctx !is Activity) {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            i.putExtra(EXTRA_ID, keyId)
            ctx.startActivity(i)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyDetailsScreen(
    key: SymmetricKeyEncrypted,
    onNavigateUp: () -> Unit,
    onConfirmKey: () -> Unit,
    onDeleteKey: () -> Unit,
    getQrBitmap: (isBlurred: Boolean) -> Bitmap?,
    onUnlockKey: (androidx.activity.result.ActivityResultLauncher<Intent>) -> Unit,
    showErrorDialog: Boolean,
    errorMessage: String,
    onDismissErrorDialog: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var isQrRevealed by remember { mutableStateOf(false) }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val isConfirmed = key.confirmedDate != null

    LaunchedEffect(Unit) {
        qrBitmap = getQrBitmap(true) // Load initial blurred QR
    }

    val unlockLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            isQrRevealed = true
            qrBitmap = getQrBitmap(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = key.name ?: "Key Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            if (!isConfirmed) {
                FloatingActionButton(onClick = { showConfirmDialog = true }) {
                    Icon(Icons.Default.Done, contentDescription = "Confirm Key")
                }
            }
        }
    ) { paddingValues ->
        KeyDetailsContent(
            modifier = Modifier.padding(paddingValues),
            key = key,
            isQrRevealed = isQrRevealed,
            qrBitmap = qrBitmap,
            onRevealQr = {
                val bitmap = getQrBitmap(false)
                if (bitmap != null) {
                    isQrRevealed = true
                    qrBitmap = bitmap
                } else {
                    onUnlockKey(unlockLauncher)
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Key") },
            text = { Text("Are you sure you want to delete this key?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteKey()
                        showDeleteDialog = false
                    }
                ) { Text("Delete") }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Key") },
            text = { Text("By confirming, you acknowledge that you have backed up this key or verified its fingerprint. This is a one-time action.") },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirmKey()
                        showConfirmDialog = false
                    }
                ) { Text("Confirm") }
            },
            dismissButton = {
                Button(onClick = { showConfirmDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = onDismissErrorDialog,
            title = { Text("Error") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(
                    onClick = onDismissErrorDialog
                ) { Text("OK") }
            }
        )
    }
}

@Composable
fun KeyDetailsContent(
    modifier: Modifier = Modifier,
    key: SymmetricKeyEncrypted,
    isQrRevealed: Boolean,
    qrBitmap: Bitmap?,
    onRevealQr: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InfoTable(key)
        Spacer(modifier = Modifier.height(24.dp))
        QrCodeSection(
            isRevealed = isQrRevealed,
            bitmap = qrBitmap,
            onReveal = onRevealQr
        )
    }
}

@Composable
fun InfoTable(key: SymmetricKeyEncrypted) {
    Column(modifier = Modifier.fillMaxWidth()) {
        InfoRow("Alias", key.name ?: "")
        InfoRow("Created", SimpleDateFormat.getDateTimeInstance().format(key.createdDate))
        InfoRow(
            "Confirmed",
            if (key.confirmedDate != null) SimpleDateFormat.getDateTimeInstance().format(key.confirmedDate) else "Not confirmed"
        )
        InfoRow("Fingerprint", SymUtil.longToPrettyHex(key.id), isMonospace = true)
    }
}

@Composable
fun InfoRow(label: String, value: String, isMonospace: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
            modifier = Modifier.weight(0.6f)
        )
    }
}


@Composable
fun QrCodeSection(
    isRevealed: Boolean,
    bitmap: Bitmap?,
    onReveal: () -> Unit
) {
    Box(
        modifier = Modifier.size(240.dp),
        contentAlignment = Alignment.Center
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Key QR Code"
            )
        } else {
            CircularProgressIndicator()
        }

        if (!isRevealed) {
            Button(
                onClick = onReveal,
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.5f))
            ) {
                Text("Reveal QR", color = Color.White, fontSize = 20.sp)
            }
        }
    }
}
