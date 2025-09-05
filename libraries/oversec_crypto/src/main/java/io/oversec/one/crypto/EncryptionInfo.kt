package io.oversec.one.crypto

enum class EncryptionInfoType {
    GPG,
    SYMMETRIC,
    SIMPLE_SYMMETRIC
}

data class EncryptionInfo(val type: EncryptionInfoType, val packageName: String?)
