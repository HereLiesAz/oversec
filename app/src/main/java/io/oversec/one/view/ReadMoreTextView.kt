package io.oversec.one.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class ReadMoreTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    fun setTrimLength(length: Int) {
        // no-op
    }

    fun setTrimCollapsedText(text: CharSequence) {
        // no-op
    }
}
