package io.oversec.one.crypto.gpg

enum class SignatureStatus {
    VALID_CONFIRMED,
    VALID_UNCONFIRMED,
    INVALID_INSECURE,
    INVALID_KEY_EXPIRED,
    INVALID_KEY_REVOKED,
    INVALID_SIGNATURE,
    KEY_MISSING,
    NO_SIGNATURE,
    UNKNOWN
}

data class SignatureVerificationInfo(
    val status: SignatureStatus,
    val keyId: Long?,
    val userId: String?
)
