package io.oversec.one.crypto

import io.oversec.one.crypto.proto.Outer

open class BaseDecryptResult {
    val isOk: Boolean = false
    val encryptionMethod: EncryptionMethod? = null
    val decryptedDataAsInnerData: Outer.InnerData = Outer.InnerData.getDefaultInstance()
}
