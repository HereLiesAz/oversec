package io.oversec.one.view

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import io.oversec.one.crypto.AbstractCryptoHandler
import io.oversec.one.crypto.BaseDecryptResult
import io.oversec.one.crypto.UserInteractionRequiredException

abstract class AbstractTextEncryptionInfoFragment : Fragment() {

    protected var mView: View? = null
    protected var mOrigText: String? = null
    protected var mTdr: BaseDecryptResult? = null
    protected var mPackagename: String? = null

    open fun setData(
        activity: EncryptionInfoActivity,
        encodedText: String,
        tdr: BaseDecryptResult?,
        uix: UserInteractionRequiredException?,
        encryptionHandler: AbstractCryptoHandler
    ) {
        mOrigText = encodedText
        mTdr = tdr
    }

    open fun onCreateOptionsMenu(activity: Activity, menu: Menu): Boolean {
        return false
    }

    open fun onPrepareOptionsMenu(menu: Menu) {
    }

    open fun onOptionsItemSelected(activity: Activity, item: MenuItem) {
    }

    protected fun share(activity: Activity, text: String, title: String) {
        // Implementation would go here if needed, or helper
    }

    fun setArgs(packagename: String?) {
        val args = Bundle()
        args.putString("packagename", packagename)
        arguments = args
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPackagename = arguments?.getString("packagename")
    }
}
