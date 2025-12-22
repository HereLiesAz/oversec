package io.oversec.one.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.R
import io.oversec.one.common.MainPreferences
import io.oversec.one.crypto.sym.OversecKeystore2
import io.oversec.one.crypto.sym.SymPreferences
import io.oversec.one.crypto.sym.SymmetricKeyEncrypted
import io.oversec.one.crypto.symbase.KeyUtil
import io.oversec.one.crypto.symbase.OversecChacha20Poly1305
import io.oversec.one.view.compose.PassphraseDialog
import java.io.IOException

class UnlockKeyActivity : AppCompatActivity() {

    private lateinit var mEncryptedKey: SymmetricKeyEncrypted

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!MainPreferences.isAllowScreenshots(this)) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        val keyId = intent.extras!!.getLong(EXTRA_KEY_ID, 0)
        val encryptedKey = OversecKeystore2.getInstance(this).getSymmetricKeyEncrypted(keyId)
        if (encryptedKey == null) {
            Log.w("UnlockKeyActivity", "something went wrong, couldn't find request key!")
            finish()
            return
        }
        mEncryptedKey = encryptedKey

        setContent {
            PassphraseDialog(
                title = getString(R.string.unlock_key_title, mEncryptedKey.name),
                originalText = null,
                onDismiss = {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                },
                onConfirm = { passphrase, ttlIndex ->
                    val timeToLiveSeconds = getTtlInSeconds(ttlIndex)
                    doOpen(passphrase.toCharArray(), timeToLiveSeconds)
                }
            )
        }
    }

    private fun getTtlInSeconds(index: Int): Int {
        return when (index) {
            0 -> 0 // Lock screen
            1 -> 5 * 60 // 5 minutes
            2 -> 30 * 60 // 30 minutes
            3 -> 60 * 60 // 1 hour
            4 -> 6 * 60 * 60 // 6 hours
            5 -> 24 * 60 * 60 // 1 day
            else -> -1 // Forever
        }
    }

    private fun doOpen(aPassPhrase: CharArray, timeToLiveSeconds: Int) {
        val aKeystore = OversecKeystore2.getInstance(this)
        SymPreferences.getInstance(this).keystoreSymTTL = timeToLiveSeconds

        Thread {
            try {
                aKeystore.doCacheKey__longoperation(
                    mEncryptedKey.id,
                    aPassPhrase,
                    timeToLiveSeconds.toLong()
                )
                setResult(Activity.RESULT_OK)
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: OversecChacha20Poly1305.MacMismatchException) {
                e.printStackTrace()
                // TODO: Show error
            } finally {
                KeyUtil.erase(aPassPhrase)
            }
        }.start()
    }

    companion object {
        val EXTRA_KEY_ID = "key_id"

        fun buildPendingIntent(ctx: Context, keyId: Long?): PendingIntent {
            val i = Intent()
            i.setClass(ctx, UnlockKeyActivity::class.java)
            i.putExtra(EXTRA_KEY_ID, keyId)

            @SuppressLint("InlinedApi") val flags = (PendingIntent.FLAG_ONE_SHOT
                    or PendingIntent.FLAG_CANCEL_CURRENT
                    or PendingIntent.FLAG_IMMUTABLE)

            return PendingIntent.getActivity(
                ctx, 0,
                i, flags
            )
        }

        fun showForResult(ctx: Context, id: Long, rq: Int) {
            val i = Intent()
            i.setClass(ctx, UnlockKeyActivity::class.java)
            i.putExtra(EXTRA_KEY_ID, id)
            if (ctx !is Activity) {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                ctx.startActivity(i)
            } else {
                i.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                ctx.startActivityForResult(i, rq)
            }
        }
    }
}