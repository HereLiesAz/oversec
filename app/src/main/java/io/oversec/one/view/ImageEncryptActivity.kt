package io.oversec.one.view

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import com.google.protobuf.ByteString
import io.oversec.one.Core
import io.oversec.one.R
import io.oversec.one.crypto.AbstractEncryptionParams
import io.oversec.one.crypto.CryptoHandlerFacade
import io.oversec.one.crypto.TemporaryContentProvider
import io.oversec.one.crypto.UserInteractionRequiredException
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.crypto.images.ImagePreferences
import io.oversec.one.crypto.images.xcoder.ContentNotFullyEmbeddedException
import io.oversec.one.crypto.images.xcoder.ImageXCoder
import io.oversec.one.crypto.images.xcoder.blackandwhite.BlackAndWhiteImageXCoder
import io.oversec.one.crypto.proto.Inner
import io.oversec.one.crypto.proto.Outer
import io.oversec.one.view.util.ImageInfo
import io.oversec.one.view.util.ImgUtil
import io.oversec.one.view.util.Util
import roboguice.util.Ln
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException

class ImageEncryptActivity : AppCompatActivity() {

    private lateinit var mCoder: ImageXCoder
    private var mPackageName: String? = null
    private var mActivityName: String? = null
    private var mImageUri: Uri? = null
    private var mFromCore: Boolean = false

    companion object {
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 43
        private const val RESAMPLE_QUALITY = 90
        private const val RQ_ENCRYPTION_PARAMS = 2
        private const val RQ_PENDING_INTENT = 3
        private var mSampleSizeS = 4 //static: We keep the last successfull sample size, assuming that images do always have the same resolution

        fun show(ctx: Activity, packagename: String, uri: Uri) {
            val i = Intent()
            i.putExtra(Util.EXTRA_PACKAGE_NAME, packagename)
            i.putExtra(Intent.EXTRA_STREAM, uri)
            i.setClass(ctx, ImageEncryptActivity::class.java)
            ctx.startActivity(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCoder = BlackAndWhiteImageXCoder(this, 2)
        init(intent, savedInstanceState == null)
        if (mPackageName == null) {
            //need to ask user first where to send it
            //get possible recipients for ACTION_SEND , but wrap the, so they will call us again, this time with EXTRA_PACKAGENAME ( and EXTRA_ACTIVITYNAME) set
            val srcIntent = Intent(Intent.ACTION_SEND)
            srcIntent.type = "image/*"

            val callback = Intent(intent)
            callback.setClassName(packageName, this.javaClass.name)
            callback.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            Util.share(this, srcIntent, callback, getString(R.string.intent_chooser_share_encrypted_image), true, OpenKeychainConnector.getInstance(this).allPackageNames(), false)
        }

        setContent {
            var showProgress by remember { mutableStateOf(false) }
            var showErrorDialog by remember { mutableStateOf<String?>(null) }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (showProgress) {
                    CircularProgressIndicator()
                }
            }

            if (showErrorDialog != null) {
                AlertDialog(
                    onDismissRequest = {
                        showErrorDialog = null
                        finish()
                    },
                    title = { Text(text = stringResource(id = R.string.error)) },
                    text = { Text(text = showErrorDialog!!) },
                    confirmButton = {
                        TextButton(onClick = {
                            showErrorDialog = null
                            finish()
                        }) {
                            Text(text = stringResource(id = android.R.string.ok))
                        }
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        Ln.d("new intent $intent")
        init(intent, false)
    }

    private fun init(intent: Intent, initial: Boolean) {
        mImageUri = intent.data
        if (mImageUri == null) {
            mImageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (mImageUri == null) {
            //received a bad intent, nothing we can do about it...
            finish()
            return
        }

        //try if we can still read it
        try {
            contentResolver.openInputStream(mImageUri!!)
        } catch (e: FileNotFoundException) {
            if (Util.checkExternalStorageAccess(this, e)) {
                ActivityCompat.requestPermissions(this@ImageEncryptActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            } else {
                showError(getString(R.string.error_image_encode_source_not_found))
            }
            return
        } catch (e: Exception) {
            showError(e.localizedMessage)
        }


        mPackageName = intent.getStringExtra(Util.EXTRA_PACKAGE_NAME)
        mActivityName = intent.getStringExtra(Util.EXTRA_ACTIVITY_NAME)

        if (mPackageName != null) {
            if (mActivityName == null) {
                //we've been called from core
                mFromCore = true
            } else {
                mFromCore = false
            }

            if (!initial || mFromCore) { //only call this on new intent or when called through the camera button
                doEncode()
            }
        }
    }

    override fun onBackPressed() {
        //the data is not necessarily coming from the camera provider,
        //but let the provider decide if he can handle this
        mImageUri?.let { TemporaryContentProvider.deleteUri(it) }

        super.onBackPressed()
    }

    private fun doEncode() {
        if (mImageUri != null) {
            ImagePreferences.getPreferences(this).setXCoder(mCoder.javaClass.simpleName)
            //there is no real way to know which encryption params we should use, so ALWAYS let the user choose or confirm!
            EncryptionParamsActivity.showForResult_ImageEncrypt(this, mPackageName!!, RQ_ENCRYPTION_PARAMS)
        } else {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //try if we can still read it
        try {
            contentResolver.openInputStream(mImageUri!!)
        } catch (e: FileNotFoundException) {
            if (Util.checkExternalStorageAccess(this, e)) {
                ActivityCompat.requestPermissions(this@ImageEncryptActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            } else {
                showError(getString(R.string.error_image_encode_source_not_found))
            }
            return
        }


        if (requestCode == RQ_ENCRYPTION_PARAMS) {
            if (resultCode == RESULT_OK) {

                try {
                    doWithEncryptionParamsSet(data)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Util.showToast(this, getString(R.string.error_image_encode_generic))
                }
            } else {
                finish()
            }
        } else if (requestCode == RQ_PENDING_INTENT) {
            try {
                doWithEncryptionParamsSet(data)

            } catch (e: Exception) {
                e.printStackTrace()
                Util.showToast(this, getString(R.string.error_image_encode_generic))
            }
        }
    }

    private fun doWithEncryptionParamsSet(encryptExtras: Intent?) {
        setContent {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        Thread {
            val params = Core.getInstance(this@ImageEncryptActivity).getLastSavedUserSelectedEncryptionParams(mPackageName!!)

            try {
                val uri = transcode(mImageUri!!, mCoder, params, encryptExtras)

                runOnUiThread {

                    if (uri != null) {
                        try {
                            share(uri)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        Util.showToast(this@ImageEncryptActivity, getString(R.string.error_image_encode_generic))
                    }
                }
            } catch (e: UserInteractionRequiredException) {
                try {
                    startIntentSenderForResult(e.pendingIntent.intentSender, RQ_PENDING_INTENT, null, 0, 0, 0)
                } catch (e1: IntentSender.SendIntentException) {
                    e1.printStackTrace()
                }
            } catch (ce: ContentNotFullyEmbeddedException) {

                //start over
                mSampleSizeS *= 2

                try {
                    doWithEncryptionParamsSet(null)
                } catch (e: GeneralSecurityException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } catch (ex: Exception) {
                ex.printStackTrace()

                runOnUiThread {
                    Util.showToast(this@ImageEncryptActivity, getString(R.string.error_image_encode_generic))
                }

            } finally {
                runOnUiThread {
                    // Hide progress
                    setContent { }
                }
            }
        }.start()
    }

    private fun share(uri: Uri) {

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.type = "image/*"
        if (mFromCore) {
            shareIntent.setPackage(mPackageName)
        } else {
            shareIntent.component = ComponentName(mPackageName!!, mActivityName!!)
        }
        startActivity(shareIntent)
        finish()

        //just in case we got this from the camera, perform an early delete!
        TemporaryContentProvider.deleteUri(mImageUri!!)
    }

    @Throws(IOException::class, GeneralSecurityException::class, UserInteractionRequiredException::class, ContentNotFullyEmbeddedException::class)
    private fun transcode(input: Uri, coder: ImageXCoder, encryptionParams: AbstractEncryptionParams, encryptExtras: Intent?): Uri? {

        val `is` = contentResolver.openInputStream(input)
        val origInfo = ImgUtil.parseImageInfo(`is`)

        val origMimeType = "image/" + origInfo.mimetype
        `is`?.close()

        var content: ByteArray? = null


        if (mSampleSizeS >= 16) {
            mSampleSizeS = 2 //reset static sample size for next try
            return null
        }


        val options = BitmapFactory.Options()
        options.inSampleSize = mSampleSizeS

        val resampledBm = BitmapFactory.decodeStream(contentResolver.openInputStream(input), null, options)

        val baoss = java.io.ByteArrayOutputStream()
        resampledBm?.compress(Bitmap.CompressFormat.JPEG, RESAMPLE_QUALITY, baoss)
        baoss.close()
        content = baoss.toByteArray()

        val idb = Inner.InnerData.newBuilder()
        val imageBuilder = idb.imageV0Builder
        imageBuilder.mimetype = origMimeType
        imageBuilder.image = ByteString.copyFrom(content)

        val data = idb.build()


        val outer = CryptoHandlerFacade.getInstance(this).encrypt(data, encryptionParams, encryptExtras)


        val aUriResult = coder.encode(outer)
        runOnUiThread {
            Util.showToast(this@ImageEncryptActivity, getString(R.string.warning_image_resized, 100 / mSampleSizeS))
        }

        return aUriResult
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                finish() //TODO implement retry
            }
        }
    }

    private fun showError(message: String?) {
        setContent {
            AlertDialog(
                onDismissRequest = { finish() },
                title = { Text(text = stringResource(id = R.string.error)) },
                text = { Text(text = message ?: "") },
                confirmButton = {
                    TextButton(onClick = { finish() }) {
                        Text(text = stringResource(id = android.R.string.ok))
                    }
                }
            )
        }
    }
}
