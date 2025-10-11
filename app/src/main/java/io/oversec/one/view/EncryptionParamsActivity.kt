package io.oversec.one.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.ui.screen.EncryptionParamsScreen

class EncryptionParamsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptionParamsScreen()
        }
    }

    companion object {
        private const val EXTRA_IME_WAS_VISIBLE = "ime_was_visible"
        private const val EXTRA_EDIT_NODE_ID = "edit_node_id"
        private const val EXTRA_PACKAGENAME = "packagename"
        private const val EXTRA_MODE = "EXTRA_MODE"
        private const val MODE_DEFAULT = "DEFAULT"
        private const val MODE_IMAGE = "IMAGE"
        private const val MODE_CLIPBOARD = "CLIPBOARD"

        fun show(
            ctx: Context,
            packagename: String?,
            editText: CharSequence?,
            nodeId: Int,
            imeWasVisible: Boolean,
            source: android.view.View?
        ) {
            val i = Intent()
            i.setClass(ctx, EncryptionParamsActivity::class.java)
            i.putExtra(EXTRA_IME_WAS_VISIBLE, imeWasVisible)
            i.putExtra(EXTRA_EDIT_NODE_ID, nodeId)
            i.putExtra(EXTRA_MODE, MODE_DEFAULT)
            i.putExtra(EXTRA_PACKAGENAME, packagename)
            i.flags = (Intent.FLAG_ACTIVITY_CLEAR_TASK
                or Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            ctx.startActivity(i)
        }

        fun showForResult_ImageEncrypt(ctx: Activity, packagename: String?, requestCode: Int) {
            val i = Intent()
            i.setClass(ctx, EncryptionParamsActivity::class.java)
            i.putExtra(EXTRA_PACKAGENAME, packagename)
            i.putExtra(EXTRA_MODE, MODE_IMAGE)
            i.flags = (Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            ctx.startActivityForResult(i, requestCode)
        }

        fun showForResult_ClipboardEncrypt(ctx: Activity, packagename: String?, requestCode: Int) {
            val i = Intent()
            i.setClass(ctx, EncryptionParamsActivity::class.java)
            i.putExtra(EXTRA_PACKAGENAME, packagename)
            i.putExtra(EXTRA_MODE, MODE_CLIPBOARD)
            i.flags = (Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                or Intent.FLAG_ACTIVITY_NO_USER_ACTION)
            ctx.startActivityForResult(i, requestCode)
        }
    }
}
