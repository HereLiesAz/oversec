package io.oversec.one.common

import android.content.Context
import io.oversec.one.R

object MainPreferences {

    val FILENAME = "main_prefs"

    fun isAllowScreenshots(ctx: Context): Boolean {
        return ctx.getSharedPreferences(FILENAME, 0)
            .getBoolean("allow_screenshot", false)
    }

    fun isRelaxEncryptionCache(ctx: Context): Boolean {
        return ctx.getSharedPreferences(FILENAME, 0)
            .getBoolean("relax_cache", false)
    }

    fun isPanicOnScreenOff(ctx: Context): Boolean {
        return ctx.getSharedPreferences(FILENAME, 0)
            .getBoolean("screenoffpanic", false)
    }

    fun isHideLauncherOnPanic(ctx: Context): Boolean {
        return ctx.getSharedPreferences(FILENAME, 0)
            .getBoolean("hidelauncheronpanic", false)
    }

    fun getLauncherSecretDialerCode(ctx: Context): String {
        return ctx.getSharedPreferences(FILENAME, 0)
            .getString(ctx.getString(R.string.mainprefs_launchersecretcode_key), "") ?: ""
    }

    fun setLauncherSecretDialerCode(ctx: Context, value: String) {
        ctx.getSharedPreferences(FILENAME, 0).edit()
            .putString(ctx.getString(R.string.mainprefs_launchersecretcode_key), value).apply()
    }

    fun isDialerSecretCodeBroadcastConfirmedWorking(ctx: Context): Boolean {
        return ctx.getSharedPreferences(FILENAME, 0).getBoolean(
            "dialersecretcodebroadcastworking5",
            false
        )
    }

    fun setDialerSecretCodeBroadcastConfirmedWorking(ctx: Context) {
        ctx.getSharedPreferences(FILENAME, 0).edit().putBoolean(
            "dialersecretcodebroadcastworking5",
            true
        ).apply()
    }
}
