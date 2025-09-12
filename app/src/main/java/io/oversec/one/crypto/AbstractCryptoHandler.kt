package io.oversec.one.crypto

abstract class AbstractCryptoHandler {
    val displayEncryptionMethod: String = ""
    fun getBinaryEncryptionInfo(a: Any?): EncryptionInfo {
        return EncryptionInfo(EncryptionInfoType.GPG, "")
    }
}
