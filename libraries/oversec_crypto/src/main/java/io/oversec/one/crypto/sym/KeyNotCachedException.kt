package io.oversec.one.crypto.sym

import io.oversec.one.crypto.UserInteractionRequiredException

<<<<<<< Updated upstream
<<<<<<< HEAD
class KeyNotCachedException(val keyId: Long?) : UserInteractionRequiredException("Key not cached: $keyId")
=======
class KeyNotCachedException(pi: PendingIntent?) : UserInteractionRequiredException(pi)
>>>>>>> origin/modernization-refactor
=======
<<<<<<< Updated upstream
class KeyNotCachedException(val keyId: Long?) : UserInteractionRequiredException("Key not cached: $keyId")
=======
class KeyNotCachedException(pi: PendingIntent?) : UserInteractionRequiredException(pi)
>>>>>>> Stashed changes
>>>>>>> Stashed changes
