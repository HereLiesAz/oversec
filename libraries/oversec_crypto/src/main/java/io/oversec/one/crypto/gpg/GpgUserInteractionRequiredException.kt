package io.oversec.one.crypto.gpg

import android.app.PendingIntent
import io.oversec.one.crypto.UserInteractionRequiredException

class GpgUserInteractionRequiredException(
    pendingIntent: PendingIntent,
    val publicKeyIds: List<Long>? = null
) : UserInteractionRequiredException(pendingIntent)
