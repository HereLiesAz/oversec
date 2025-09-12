package io.oversec.one.crypto.images.xcoder.blackandwhite

import android.graphics.Bitmap

class BlackAndWhiteImageXCoder {
    fun encode(data: ByteArray, bitmap: Bitmap): Bitmap {
        return bitmap
    }

    fun decode(bitmap: Bitmap): ByteArray {
        return ByteArray(0)
    }
}
