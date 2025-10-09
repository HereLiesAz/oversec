package io.oversec.one.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.iab.IabUtil
import io.oversec.one.ui.screen.AboutScreen
import io.oversec.one.ui.theme.OneTheme

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isIabAvailable = IabUtil.getInstance(this).isIabAvailable

        setContent {
            OneTheme {
                AboutScreen(
                    isIabAvailable = isIabAvailable,
                    onBackPressed = { finish() }
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun show(ctx: Context) {
            val i = Intent(ctx, AboutActivity::class.java)
            ctx.startActivity(i)
        }
    }
}