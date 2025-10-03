package io.oversec.one.view

import androidx.appcompat.app.AppCompatActivity
import io.oversec.one.R

abstract class BaseActivity : AppCompatActivity() {
    protected fun showError(msg: String, runnable: Runnable?) {
        // TODO: This used the removed MaterialDialogs library.
        // Calling activities should implement their own error handling.
        // runOnUiThread {
        //     MaterialDialog.Builder(this@BaseActivity)
        //         .title(R.string.common_error_title)
        //         .iconRes(R.drawable.ic_error_black_24dp)
        //         .cancelable(true)
        //         .content(msg)
        //         .neutralText(R.string.common_ok)
        //         .cancelListener { dialog ->
        //             dialog.dismiss()
        //             runnable?.run()
        //         }
        //         .onNeutral { dialog, which ->
        //             dialog.dismiss()
        //             runnable?.run()
        //         }
        //         .show()
        // }
    }
}
