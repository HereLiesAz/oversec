package io.oversec.one.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.oversec.one.crypto.BaseDecryptResult
import io.oversec.one.crypto.images.xcoder.ImageXCoder
import io.oversec.one.crypto.proto.Outer

abstract class AbstractBinaryEncryptionInfoFragment : Fragment() {

    protected var mView: View? = null
    protected var mMsg: Outer.Msg? = null
    protected var mTdr: BaseDecryptResult? = null
    protected var mCoder: ImageXCoder? = null
    protected var mPackagename: String? = null

    open fun handleSetData(msg: Outer.Msg, tdr: BaseDecryptResult?, coder: ImageXCoder?) {
        mMsg = msg
        mTdr = tdr
        mCoder = coder
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
