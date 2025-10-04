package io.oversec.one.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.R
import io.oversec.one.crypto.sym.SymPreferences
import io.oversec.one.crypto.sym.SymmetricKeyPlain
import io.oversec.one.crypto.symbase.KeyCache
import io.oversec.one.crypto.symbase.KeyUtil
import io.oversec.one.crypto.symsimple.PasswordCantDecryptException
import io.oversec.one.crypto.symsimple.SimpleSymmetricCryptoHandler
import io.oversec.one.view.compose.PassphraseDialog
import java.io.IOException
import java.security.NoSuchAlgorithmException
import java.util.Arrays
import java.util.Date
import kotlin.math.max

class AddPasswordKeyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PassphraseDialog(
                title = getString(R.string.simplesym_add_password_title),
                originalText = intent.getStringExtra(EXTRA_ENCRYPTED_TEXT),
                onDismiss = {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                },
                onConfirm = { passphrase, ttlIndex ->
                    val timeToLiveSeconds = getTtlInSeconds(ttlIndex)
                    var sessionKeyIds: LongArray? = null
                    var sessionKeySalts: Array<ByteArray>? = null
                    var sessionKeyCost = 0
                    if (intent.extras != null && intent.extras!!.get(EXTRA_KEYHASH_ID) != null) {
                        sessionKeyIds = intent.extras!!.getLongArray(EXTRA_KEYHASH_ID)
                        val xx = intent.extras!!.getSerializable(EXTRA_KEYHASH_SALT) as Array<Any>
                        sessionKeySalts = Array(xx.size) { i -> xx[i] as ByteArray }
                        sessionKeyCost = intent.extras!!.getInt(EXTRA_KEYHASH_COST)
                    }
                    doOpen(
                        passphrase.toCharArray(),
                        timeToLiveSeconds,
                        sessionKeyIds,
                        sessionKeySalts,
                        sessionKeyCost
                    )
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

    private fun doOpen(
        aPassPhrase: CharArray,
        timeToLiveSeconds: Int,
        expectedKeyIdHashes: LongArray?,
        saltsForKeyHash: Array<ByteArray>?,
        costForKeyHash: Int
    ) {
        SymPreferences.getInstance(this).keystoreSimpleTTL = timeToLiveSeconds

        Thread {
            try {
                val keyId = addPasswordToCache__longoperation(
                    aPassPhrase,
                    timeToLiveSeconds,
                    expectedKeyIdHashes,
                    saltsForKeyHash,
                    costForKeyHash,
                    KeyCache.getInstance(this)
                )

                val data = Intent()
                data.putExtra(EXTRA_RESULT_KEY_ID, keyId)
                setResult(Activity.RESULT_OK, data)
                finish()
            } catch (e: PasswordCantDecryptException) {
                // TODO: Show error
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Show error
            } finally {
                KeyUtil.erase(aPassPhrase)
            }
        }.start()
    }

    companion object {
        const val EXTRA_RESULT_KEY_ID = "EXTRA_RESULT_KEY_ID"
        private const val EXTRA_KEYHASH_ID = "EXTRA_KEYHASH_ID"
        private const val EXTRA_KEYHASH_SALT = "EXTRA_KEYHASH_SALT"
        private const val EXTRA_KEYHASH_COST = "EXTRA_KEYHASH_COST"
        private const val EXTRA_ENCRYPTED_TEXT = "EXTRA_ENCRYPTED_TEXT"
    }

    @Throws(
        NoSuchAlgorithmException::class,
        IOException::class,
        PasswordCantDecryptException::class
    )
    private fun addPasswordToCache__longoperation(
        pw: CharArray,
        ttl: Int,
        expectedKeyIdHashes: LongArray?,
        saltsForKeyHash: Array<ByteArray>?,
        costForKeyHash: Int,
        keyCache: KeyCache
    ): Long? {
        val keyName = pw[0] + "*".repeat(max(pw.size - 2, 0)) + pw[pw.size - 1]

        val rawkey = KeyUtil.brcryptifyPassword(
            pw,
            SimpleSymmetricCryptoHandler.KEY_DERIVATION_SALT,
            SimpleSymmetricCryptoHandler.KEY_DERIVATION_COST,
            32
        )
        KeyUtil.erase(pw)

        val id = KeyUtil.calcKeyId(
            Arrays.copyOf(rawkey, rawkey.size),
            SimpleSymmetricCryptoHandler.KEY_ID_COST
        )

        if (expectedKeyIdHashes != null) {
            var match = false
            for (i in expectedKeyIdHashes.indices) {
                val hash = KeyUtil.calcSessionKeyId(id, saltsForKeyHash!![i], costForKeyHash)
                if (hash == expectedKeyIdHashes[i]) {
                    match = true
                    break
                }
            }
            if (!match) {
                throw PasswordCantDecryptException()
            }
        }

        val key = SymmetricKeyPlain(id, keyName, Date(), rawkey, true)
        keyCache.doCacheKey(key, ttl.toLong())
        return id
    }
}