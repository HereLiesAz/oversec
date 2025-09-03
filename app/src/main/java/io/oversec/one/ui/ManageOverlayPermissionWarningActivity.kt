package io.oversec.one.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.oversec.one.R
import java.util.concurrent.TimeUnit

class ManageOverlayPermissionWarningActivity : AppCompatActivity() {

    companion object {
        private var LATER: Long = 0

        fun show(ctx: Context) {
            if (System.currentTimeMillis() < LATER) {
                return
            }
            val i = Intent()
            i.setClass(ctx, ManageOverlayPermissionWarningActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OverlayPermissionWarningScreen(
                onGoClick = {
                    val i = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    i.data = Uri.parse("package:$packageName")
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(i)
                },
                onLaterClick = {
                    LATER = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1)
                    finish()
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(this)) {
                finish()
            }
        }
    }
}

@Composable
fun OverlayPermissionWarningScreen(onGoClick: () -> Unit, onLaterClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.overlay_permission_warning))
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = onGoClick) {
                Text(text = stringResource(id = R.string.go_to_settings))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = onLaterClick) {
                Text(text = stringResource(id = R.string.later))
            }
        }
    }
}
