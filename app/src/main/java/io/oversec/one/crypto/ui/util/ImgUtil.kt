package io.oversec.one.crypto.ui.util

import android.content.ContentResolver
import android.net.Uri

object ImgUtil {
    fun getImageInfo(contentResolver: ContentResolver, uri: Uri?): ImageInfo {
        return ImageInfo(1, 2, 3)
    }
}
