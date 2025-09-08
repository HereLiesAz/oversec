package io.oversec.one.crypto

import android.content.Context
import android.content.Intent
import io.oversec.one.crypto.proto.Inner
import io.oversec.one.crypto.proto.Outer
import java.io.IOException
import java.security.GeneralSecurityException


abstract class AbstractCryptoHandler(protected val mCtx: Context) {

    @Throws(
        GeneralSecurityException::class,
        UserInteractionRequiredException::class,
        IOException::class
    )
    abstract fun encrypt(
        innerData: Inner.InnerData,
        params: AbstractEncryptionParams,
        actionIntent: Intent?
    ): Outer.Msg?

    @Throws(
        GeneralSecurityException::class,
        UserInteractionRequiredException::class,
        IOException::class
    )
    abstract fun encrypt(
        plainText: String,
        params: AbstractEncryptionParams,
        actionIntent: Intent?
    ): Outer.Msg?

    @Throws(UserInteractionRequiredException::class)
    abstract fun decrypt(
        msg: Outer.Msg,
        actionIntent: Intent?,
        encryptedText: String?
    ): BaseDecryptResult?

    abstract fun buildDefaultEncryptionParams(tdr: BaseDecryptResult): AbstractEncryptionParams
}
