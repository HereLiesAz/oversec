package it.sephiroth.android.library.imagezoom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

open class ImageViewTouchBase(context: Context, attrs: AttributeSet?) : ImageView(context, attrs) {
    enum class DisplayType {
        FIT_TO_SCREEN
    }
}
