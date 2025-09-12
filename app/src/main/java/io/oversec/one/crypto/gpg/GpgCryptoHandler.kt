package io.oversec.one.crypto.gpg

import android.content.Context
import android.content.Intent
import io.oversec.one.crypto.AbstractCryptoHandler
import org.openintents.openpgp.OpenPgpSignatureResult

class GpgCryptoHandler : AbstractCryptoHandler() {
    companion object {
        fun signatureResultToUiText(context: Context?, sr: OpenPgpSignatureResult): String {
            return ""
        }

        fun signatureResultToUiIconRes(sr: OpenPgpSignatureResult, b: Boolean): Int {
            return 0
        }

        fun signatureResultToUiColorResId(sr: OpenPgpSignatureResult): Int {
            return 0
        }

        fun signatureResultToUiIconRes_KeyOnly(sr: OpenPgpSignatureResult, b: Boolean): Int {
            return 0
        }

        fun signatureResultToUiColorResId_KeyOnly(sr: OpenPgpSignatureResult): Int {
            return 0
        }

        fun openOpenKeyChain(context: Context?) {
            // stub
        }
    }

    fun getFirstUserIDByKeyId(pkid: Long, a: Any?): String? {
        return null
    }

    fun getDownloadKeyPendingIntent(keyId: Long, actionIntent: Intent?): Intent? {
        return null
    }
}
