package io.oversec.one.ui.screen

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    var isEnabled: Boolean
)