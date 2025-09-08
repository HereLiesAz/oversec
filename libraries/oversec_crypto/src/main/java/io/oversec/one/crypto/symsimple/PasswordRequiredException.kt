package io.oversec.one.crypto.symsimple

import io.oversec.one.crypto.UserInteractionRequiredException

class PasswordRequiredException(
    val keyHashes: LongArray,
    val salts: Array<ByteArray>,
    val sessionKeyCost: Int,
    val encryptedText: String?
) : UserInteractionRequiredException("Password required to decrypt.")
