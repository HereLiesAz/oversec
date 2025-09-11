package io.oversec.one.crypto

<<<<<<< HEAD
open class UserInteractionRequiredException(message: String? = null) : Exception(message)
=======
import android.app.PendingIntent

open class UserInteractionRequiredException : Exception {

    val pendingIntent: PendingIntent?
    private var mPublicKeyIds: LongArray? = null  //TODO: not longer used ?!

    constructor(pi: PendingIntent?, pkids: List<Long>?) {
        pendingIntent = pi
        mPublicKeyIds = pkids?.toLongArray()
    }

    constructor(pi: PendingIntent?, pkids: LongArray) {
        pendingIntent = pi
        mPublicKeyIds = pkids
    }


    constructor(pi: PendingIntent?) {
        pendingIntent = pi
    }
}
>>>>>>> origin/modernization-refactor
