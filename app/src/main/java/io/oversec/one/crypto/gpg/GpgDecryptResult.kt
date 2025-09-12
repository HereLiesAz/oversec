package io.oversec.one.crypto.gpg

import android.content.Intent
import io.oversec.one.crypto.BaseDecryptResult
import org.openintents.openpgp.OpenPgpSignatureResult

class GpgDecryptResult : BaseDecryptResult() {
    val signatureResult: OpenPgpSignatureResult? = null
    val downloadMissingSignatureKeyPendingIntent: Intent? = null
    val showSignatureKeyPendingIntent: Intent? = null
}
