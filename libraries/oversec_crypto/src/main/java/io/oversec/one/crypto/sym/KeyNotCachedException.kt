package io.oversec.one.crypto.sym

import io.oversec.one.crypto.UserInteractionRequiredException

<<<<<<< HEAD
class KeyNotCachedException(val keyId: Long?) : UserInteractionRequiredException("Key not cached: $keyId")
=======
class KeyNotCachedException(pi: PendingIntent?) : UserInteractionRequiredException(pi)
>>>>>>> origin/modernization-refactor
