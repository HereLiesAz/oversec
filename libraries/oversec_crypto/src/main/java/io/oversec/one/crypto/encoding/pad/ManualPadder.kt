package io.oversec.one.crypto.encoding.pad

import android.content.Context

class ManualPadder internal constructor(ctx: Context) : AbstractPadder(ctx) {

    @Synchronized
    override fun pad(orig: String, encoded: StringBuffer) {
    }

    override fun reset() {}

    override val nextPaddingChar: Char
        get() = 0.toChar()

    override fun tail(): String {
        return ""
    }

    override val id
        get() = "Manual"

    override val label
        get() = "Manual"

    override val example
        get() = "(manually enter text to pad)"

}
