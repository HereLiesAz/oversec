package io.oversec.one.view

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.common.CoreContract
import io.oversec.one.crypto.Help
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.db.PadderDb
import io.oversec.one.ui.screen.PadderDetailScreen
import io.oversec.one.ui.theme.OneTheme

class PadderDetailActivity : AppCompatActivity() {

    private lateinit var db: PadderDb
    private var padder: PadderContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = PadderDb.getInstance(this)

        val padderId = intent.getLongExtra(EXTRA_ID, 0)
        if (padderId != 0L) {
            padder = db.get(padderId)
        }

        setContent {
            OneTheme {
                PadderDetailScreen(
                    padder = padder,
                    onSave = { name, content ->
                        CoreContract.getInstance().doIfFullVersionOrShowPurchaseDialog(this, {
                            save(name, content)
                        }, RQ_UPGRADE)
                    },
                    onDelete = {
                        CoreContract.getInstance().doIfFullVersionOrShowPurchaseDialog(this, {
                            delete()
                        }, RQ_UPGRADE)
                    },
                    onHelp = {
                        Help.INSTANCE.open(this, Help.ANCHOR.main_padders)
                    },
                    onBackPressed = {
                        finish()
                    }
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RQ_UPGRADE) {
            if (resultCode == RESULT_OK) {
                // The save/delete will be re-triggered by the user clicking the button again.
            }
        }
    }

    private fun save(name: String, content: String) {
        val pc = padder
        if (pc == null) {
            val newPadder = PadderContent(name, content)
            db.add(newPadder)
        } else {
            pc.name = name
            pc.content = content
            pc.sort = name
            db.update(pc)
        }
        setResult(RESULT_OK)
        finish()
    }

    private fun delete() {
        padder?.let {
            db.delete(it.key)
            setResult(RESULT_OK)
            finish()
        }
    }

    companion object {
        private const val EXTRA_ID = "EXTRA_ID"
        private const val RQ_UPGRADE = 9001

        @JvmStatic
        fun showForResult(frag: Fragment, requestCode: Int, id: Long?) {
            val i = Intent(frag.activity, PadderDetailActivity::class.java)
            if (id != null) {
                i.putExtra(EXTRA_ID, id)
            }
            frag.startActivityForResult(i, requestCode)
        }
    }
}