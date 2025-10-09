package io.oversec.one.view

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.crypto.Help
import io.oversec.one.db.PadderDb
import io.oversec.one.ui.screen.PadderDetailScreen
import io.oversec.one.ui.theme.OneTheme

class PadderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val padderId = intent.getLongExtra(EXTRA_ID, 0L).takeIf { it != 0L }

        setContent {
            OneTheme {
                PadderDetailScreen(
                    padderId = padderId,
                    db = PadderDb.getInstance(this),
                    onHelp = {
                        Help.INSTANCE.open(this, Help.ANCHOR.main_padders)
                    },
                    onBackPressed = {
                        finish()
                    },
                    onSaveFinished = {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}