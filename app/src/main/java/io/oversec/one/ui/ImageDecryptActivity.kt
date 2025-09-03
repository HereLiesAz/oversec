package io.oversec.one.ui

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.protobuf.ByteString
import com.google.protobuf.InvalidProtocolBufferException
import io.oversec.one.R
import io.oversec.one.crypto.*
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.crypto.images.xcoder.ImageXCoder
import io.oversec.one.crypto.images.xcoder.ImageXCoderFacade
import io.oversec.one.crypto.proto.Outer
import io.oversec.one.crypto.ui.AbstractBinaryEncryptionInfoFragment
import io.oversec.one.crypto.ui.util.Util
import io.oversec.one.iab.FullVersionListener
import io.oversec.one.iab.IabUtil
import io.oversec.one.ui.encparams.ActivityResultWrapper
import it.sephiroth.android.library.imagezoom.ImageViewTouch
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase
import roboguice.util.Ln
import java.io.IOException
import java.io.OutputStream

class ImageDecryptActivity : AppCompatActivity() {

    private var mVgError: ViewGroup? = null
    private var mVgProgress: ViewGroup? = null
    private var mIvFull: ImageViewTouch? = null
    private var mIvThumb: ImageView? = null
    private var mVgMain: ViewGroup? = null
    private var mBitmapRef: Bitmap? = null
    private var mBitmapPlain: Bitmap? = null
    private var mBitmapBlurred: Bitmap? = null
    private var mTempActivityResult: ActivityResultWrapper? = null
    private var mZoomedIn: Boolean = false
    private var mVgUpgrade: ViewGroup? = null
    private var mTvError: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decrypt_image)

        mVgError = findViewById(R.id.vg_error)
        mTvError = findViewById(R.id.tv_error)
        mVgUpgrade = findViewById(R.id.vg_upgrade_reminder)
        mVgProgress = findViewById(R.id.vg_progress)
        mVgMain = findViewById(R.id.vg_main)
        mIvFull = findViewById(R.id.iv_full)
        mIvThumb = findViewById(R.id.iv_thumb)


        if (savedInstanceState != null) {
            mZoomedIn = savedInstanceState.getBoolean(EXTRA_ZOOMED)
        }
        init()
    }

    private fun init() {
        mVgError?.visibility = View.GONE
        mVgUpgrade?.visibility = View.GONE
        mVgMain?.visibility = View.GONE
        mIvFull?.visibility = View.GONE
        mVgProgress?.visibility = View.GONE


        mVgProgress?.visibility = View.VISIBLE
        Thread { handleReceiveImage(intent, null) }.start()

        handleActivityResult()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_image_decrypted, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return mBitmapRef != null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_send_plain) {
            share(Intent.ACTION_SEND)
        } else if (id == R.id.action_view_plain) {
            share(Intent.ACTION_VIEW)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun share(action: String) {
        try {
            val uri = TemporaryContentProvider.prepare(this, "image/jpeg", TemporaryContentProvider.TTL_5_MINUTES, TemporaryContentProvider.TAG_DECRYPTED_IMAGE) //TODO make configurable
            val os = contentResolver.openOutputStream(uri)
            mBitmapPlain?.compress(Bitmap.CompressFormat.PNG, 100, os)
            os?.close()


            val shareIntent = Intent()
            shareIntent.action = action
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/*"

            Util.share(this, shareIntent, null, getString(R.string.intent_chooser_share_unencrypted_image), true, OpenKeychainConnector.getInstance(this).allPackageNames(), false)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    internal fun handleReceiveImage(intent: Intent, decryptExtras: Intent?) {
        var msg: Outer.Msg? = null
        var dr: BaseDecryptResult? = null
        var xcoder: ImageXCoder? = null
        var decodedBitmap: Bitmap? = null
        var error: String? = null

        var imageUri: Uri? = intent.data

        if (imageUri == null) {
            imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        }
        if (imageUri != null) {

            for (xc in ImageXCoderFacade.getAll(this)) {
                try {
                    msg = xc.parse(imageUri)
                    if (msg == null || msg.msgDataCase == Outer.Msg.MsgDataCase.MSGDATA_NOT_SET) {
                        continue
                    } else {
                        xcoder = xc
                        break
                    }
                } catch (ex: IOException) {
                    ex.printStackTrace()
                    if (Util.checkExternalStorageAccess(this, ex)) {
                        ActivityCompat.requestPermissions(this@ImageDecryptActivity,
                                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                        return
                    }
                } catch (exx: Exception) {
                    exx.printStackTrace()
                }
            }
        }


        if (xcoder == null || msg == null) {
            error = getString(R.string.error_image_decrypt_noencodeddata)
        } else {
            try {
                dr = CryptoHandlerFacade.getInstance(this).decrypt(msg, decryptExtras, null)
            } catch (ex: UserInteractionRequiredException) {
                try {
                    startIntentSenderForResult(ex.pendingIntent.intentSender, RQ_DECRYPT, null, 0, 0, 0)
                } catch (e: IntentSender.SendIntentException) {
                    Ln.e(e, "error sending pending intent")
                }
                return
            }

            if (dr != null && dr.isOk) {
                try {
                    val img = dr.decryptedDataAsInnerData.imageV0.image
                    val buf = img.toByteArray()
                    decodedBitmap = BitmapFactory.decodeByteArray(buf, 0, buf.length)

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if (decodedBitmap == null) {
                    error = getString(R.string.error_image_decrypt_generic)
                }
            } else {

                error = getString(R.string.error_image_decrypt_nomatchingkey)
            }

        }


        if (error == null) {
            val bm = decodedBitmap
            val xc = xcoder
            val outer = msg
            val dec = dr
            runOnUiThread { publishResult_UI(bm!!, xc!!, outer!!, dec!!) }
        } else {
            val ferror = error
            runOnUiThread {
                mTvError?.text = ferror
                mVgError?.visibility = View.VISIBLE
                mVgMain?.visibility = View.GONE
                mIvFull?.visibility = View.GONE
                mVgProgress?.visibility = View.GONE
            }

        }


    }


    private fun handleActivityResult() {
        if (mTempActivityResult != null) {
            val requestCode = mTempActivityResult!!.requestCode
            val resultCode = mTempActivityResult!!.resultCode
            val data = mTempActivityResult!!.data
            mTempActivityResult = null

            if (requestCode == RQ_DECRYPT && resultCode == RESULT_OK) {
                handleReceiveImage(intent, data)
            } else {
                if (requestCode == RQ_UPGRADE) {
                    init()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mTempActivityResult = ActivityResultWrapper(requestCode, resultCode, data)

        //DAMNIT the liefecycle is indeed different whether activity is destroyed and recreated or just stays around!
        if (mVgMain != null) {
            handleActivityResult()
        } else {
            //will be handled in onCreateView
        }
    }

    private fun publishResult_UI(bm: Bitmap, xc: ImageXCoder, outer: Outer.Msg, dec: BaseDecryptResult) {
        try {
            mVgError?.visibility = View.GONE
            mVgMain?.visibility = View.VISIBLE
            mIvFull?.visibility = View.GONE
            mVgProgress?.visibility = View.GONE

            mBitmapPlain = bm

            val bmBlurred = Bitmap.createScaledBitmap(bm, 15, 15, true)
            //TODO recycle!!
            mBitmapBlurred = bmBlurred

            mBitmapRef = bmBlurred

            mVgUpgrade?.visibility = View.VISIBLE
            val btUpgrade = findViewById<Button>(R.id.btn_upgrade)
            btUpgrade.setOnClickListener { IabUtil.getInstance(this@ImageDecryptActivity).showPurchaseActivity(this@ImageDecryptActivity, RQ_UPGRADE) }
            mIvThumb?.setImageBitmap(bmBlurred)

            mIvThumb?.setOnClickListener { zoomIn() }

            if (mZoomedIn) {
                zoomIn()
            }

            var aFragment: AbstractBinaryEncryptionInfoFragment? = null
            val encryptionHandler = CryptoHandlerFacade.getInstance(this).getCryptoHandler(dec.encryptionMethod)
            if (encryptionHandler != null) {
                aFragment = encryptionHandler.getBinaryEncryptionInfoFragment(null)
            }

            if (aFragment == null) {
                finish()
            } else {

                aFragment.setArgs(null)

                val manager = fragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(io.oversec.one.crypto.R.id.encryptionInfoFragment_container, aFragment, "Foo")

                transaction.commit()


                aFragment.setData(outer, dec, xc)

            }


            IabUtil.getInstance(this).checkFullVersion(object : FullVersionListener {
                override fun onFullVersion_MAIN_THREAD(isFullVersion: Boolean) {
                    if (isFullVersion) {
                        runOnUiThread {
                            mVgUpgrade?.visibility = View.GONE
                            mBitmapRef = bm
                            mIvThumb?.setImageBitmap(mBitmapPlain)
                        }
                    }
                }
            })


        } catch (ex: Exception) {
            //TODO: implement better state synchronization,
            //weird stuff might happen if user rotates device while decrypting.
            Ln.e(ex, "damnit,")
        }
    }

    private fun zoomIn() {
        mVgMain?.visibility = View.GONE
        mIvFull?.visibility = View.VISIBLE
        mIvFull?.setImageBitmap(mBitmapRef, null, 0.1f, 100f)
        mIvFull?.displayType = ImageViewTouchBase.DisplayType.FIT_TO_SCREEN
    }

    override fun onBackPressed() {
        if (mIvFull?.visibility == View.VISIBLE) {
            mVgMain?.visibility = View.VISIBLE
            mIvFull?.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(EXTRA_ZOOMED, mIvFull?.visibility == View.VISIBLE)
    }

    override fun onDestroy() {
        if (mBitmapBlurred != null) {
            mBitmapBlurred!!.recycle()
        }
        if (mBitmapPlain != null) {
            mBitmapPlain!!.recycle()
        }
        super.onDestroy()
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

    companion object {
        private const val RQ_DECRYPT = 1
        private const val EXTRA_ZOOMED = "EXTRA_ZOOMED"
        private const val RQ_UPGRADE = 8801
        private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 43
    }
}
