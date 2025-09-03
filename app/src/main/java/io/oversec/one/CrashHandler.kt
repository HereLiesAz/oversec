package io.oversec.one

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.io.StringWriter

object CrashHandler {

    fun init(app: App) {
        val defaultUeh = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { thread, ex ->

            println("********************* CRASH ******************* ")
            ex.printStackTrace()

            val stackTrace = buildStackTrace(ex)
            val logcat = grabLogcat()

            if (!hasFlagFile(app.applicationContext)) {

                val am = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = CrashActivity.buildIntent(app, thread.name, stackTrace + "\nLOGCAT:\n" + logcat)
                val pi = PendingIntent.getActivity(app, 0, intent, 0)

                //TODO: maybe write some counter value to disk and prevent an endless loop ??

                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 2000, pi)

                try {
                    writeFlagFile(app.applicationContext)
                } catch (ex2: Exception) {
                }

                if (!BuildConfig.DEBUG) {
                    System.exit(0)
                }
            } else {
                //we have a flag file, i.e. the app crashed previously, so if it crashes again we just
                //call the standard exception handler to prevent loops,
                //but remove the flag file so next time it'll work again!
                try {
                    removeFlagFile(app.applicationContext)
                } catch (ex2: Exception) {
                }
                defaultUeh?.uncaughtException(thread, ex)
            }
        }
    }

    fun removeFlagFile(context: Context) {
        getFlagFile(context).delete()
    }

    private fun writeFlagFile(context: Context) {
        try {
            getFlagFile(context).createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getFlagFile(context: Context): File {
        return File(context.cacheDir, ".crashed")
    }

    private fun hasFlagFile(context: Context): Boolean {
        return getFlagFile(context).exists()
    }

    private fun buildStackTrace(ex: Throwable): String {
        val result = StringWriter()
        val printWriter = PrintWriter(result)
        ex.printStackTrace(printWriter)
        return result.toString()
    }

    private fun grabLogcat(): String? {
        return try {
            val process = Runtime.getRuntime().exec("logcat -d")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))

            val log = StringBuilder()
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                log.append(line)
                log.append("\n")
            }

            log.toString()

        } catch (e: IOException) {
            null
        }
    }
}
