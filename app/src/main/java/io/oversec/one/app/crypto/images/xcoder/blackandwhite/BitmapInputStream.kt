package io.oversec.one.app.crypto.images.xcoder.blackandwhite

import android.graphics.Bitmap
import java.io.InputStream

class BitmapInputStream(bm: Bitmap, pixels: Int) : InputStream() {
    override fun read(): Int {
        return -1
    }
}
