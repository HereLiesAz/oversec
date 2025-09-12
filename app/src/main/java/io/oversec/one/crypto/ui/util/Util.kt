package io.oversec.one.crypto.ui.util

import android.content.Context

object Util {
    fun isOversec(context: Context): Boolean {
        return true
    }

    fun getPackageLabel(context: Context, packageName: String): String {
        return "dummy label"
    }

    fun longToPrettyHex(l: Long): String {
        return "0x" + java.lang.Long.toHexString(l)
    }
}
