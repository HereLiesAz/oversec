package org.openintents.openpgp

class OpenPgpSignatureResult {
    var result: Int = 0
    var primaryUserId: String = ""
    var keyId: Long = 0

    companion object {
        const val RESULT_INVALID_SIGNATURE = 0
        const val RESULT_KEY_MISSING = 1
        const val RESULT_NO_SIGNATURE = 2
        const val RESULT_INVALID_INSECURE = 3
        const val RESULT_INVALID_KEY_EXPIRED = 4
        const val RESULT_INVALID_KEY_REVOKED = 5
        const val RESULT_VALID_UNCONFIRMED = 6
        const val RESULT_VALID_CONFIRMED = 7
    }
}
