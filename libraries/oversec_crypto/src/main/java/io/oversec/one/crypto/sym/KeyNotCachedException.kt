package io.oversec.one.crypto.sym

import io.oversec.one.crypto.UserInteractionRequiredException

class KeyNotCachedException(val keyId: Long?) : UserInteractionRequiredException("Key not cached: $keyId")
