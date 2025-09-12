package io.oversec.one.crypto

import android.content.Context

class CryptoHandlerFacade {
    companion object {
        fun getInstance(context: Context?): CryptoHandlerFacade {
            return CryptoHandlerFacade()
        }
    }

    fun getCryptoHandler(tdr: BaseDecryptResult): AbstractCryptoHandler? {
        return null
    }

    fun getCryptoHandler(method: EncryptionMethod): AbstractCryptoHandler? {
        return null
    }
}
