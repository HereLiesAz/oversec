package io.oversec.one.view.util

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.hardware.Camera
import androidx.core.content.ContextCompat
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import java.io.IOException
import java.util.ArrayList

object Util {

    const val EXTRA_PACKAGE_NAME = "packagename"
    const val EXTRA_ACTIVITY_NAME = "activityname"


    fun share(
        src: Activity,
        srcIntent: Intent,
        callbackIntent: Intent?,
        title: String,
        filter: IPackageNameFilter,
        finishActivity: Boolean
    ) {
        val pm = src.packageManager
        val resInfo = pm.queryIntentActivities(srcIntent, 0)
        val targetIntents = ArrayList<Intent>()

        for (ri in resInfo) {
            val packageName = ri.activityInfo.packageName
            if (!filter.include(packageName)) {
                continue
            }
            val intent = Intent(srcIntent)
            intent.component = ComponentName(ri.activityInfo.packageName, ri.activityInfo.name)
            targetIntents.add(intent)
        }

        if (targetIntents.isEmpty()) {
            return
        }

        val chooserIntent = Intent.createChooser(targetIntents.removeAt(0), title)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toTypedArray())
        src.startActivity(chooserIntent)
        if (finishActivity) {
            src.finish()
        }
    }


    interface IPackageNameFilter {
        fun include(packageName: String): Boolean
    }

    fun share(
        src: Activity,
        srcIntent: Intent,
        callbackIntent: Intent?,
        title: String,
        excludeOwnPackage: Boolean,
        excludeOtherPackages: Set<String>?,
        finishActivity: Boolean
    ) {
        share(src, srcIntent, callbackIntent, title, object : IPackageNameFilter {
            override fun include(packageName: String): Boolean {

                return if (excludeOwnPackage && src.packageName == packageName) {
                    //not including our own intents
                    false
                } else !(excludeOtherPackages != null && excludeOtherPackages.contains(
                    packageName
                ))
            }
        }, finishActivity)

    }

    fun share(
        src: Activity,
        srcIntent: Intent,
        callbackIntent: Intent,
        title: String,
        onlyThisPackageName: String?,
        finishActivity: Boolean
    ) {

        share(src, srcIntent, callbackIntent, title, object : IPackageNameFilter {
            override fun include(packageName: String): Boolean {
                return onlyThisPackageName == null || onlyThisPackageName == packageName
            }
        }, finishActivity)

    }

    fun showToast(ctx: Context, s: String) {
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show()
    }


    fun checkCameraAccess(ctx: Context): Boolean {
        var ret = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED


        if (ret) {
            //we still can't be sure, android misreport permission when revoked through settings
            var aCamera: Camera? = null
            try {
                aCamera = Camera.open()
                val mParameters = aCamera!!.parameters
                aCamera.parameters = mParameters
            } catch (e: Exception) {
                ret = false
            }

            aCamera?.release()
        }
        return ret
    }


    fun checkExternalStorageAccess(ctx: Context, e: IOException): Boolean {
        return ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && e.message!!.contains("App op not allowed")
    }

}
