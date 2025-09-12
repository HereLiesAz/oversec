package io.oversec.one.crypto

import android.content.Context
import android.net.Uri
import java.io.File

object TemporaryContentProvider {
    fun getUriForFile(context: Context, file: File): Uri {
        return Uri.fromFile(file)
    }
}
