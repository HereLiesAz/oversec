package io.oversec.one.view

import android.content.*
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.IBinder
import android.os.RemoteException
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import io.oversec.one.IZxcvbnService
import io.oversec.one.R
import io.oversec.one.ZxcvbnService
import io.oversec.one.view.util.EditTextPasswordWithVisibilityToggle
import uk.co.biddell.diceware.dictionaries.DiceWare
import java.io.IOException

object NewPasswordInputDialog {
    private const val ENTROPY_MEDIUM = 45
    private const val ENTROPY_HIGH_SHARE = 75
    private const val ENTROPY_HIGH_PBKDF = 75
    private const val ENTROPY_HIGH_DEVICE = 60

    private const val DICEWARE_WORDS_KEYSTORE = 4
    private const val DICEWARE_WORDS_SHARE = 5
    private const val DICEWARE_WORDS_PBKDF = 6

    enum class MODE {
        SHARE, KEYSTORE, PBKDF
    }

    fun show(ctx: Context, mode: MODE, callback: NewPasswordInputDialogCallback) {
        val serviceIntent = Intent(ctx, ZxcvbnService::class.java)
        ctx.startService(serviceIntent)
        val mZxcvbnService = arrayOfNulls<IZxcvbnService>(1)
        val mConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                mZxcvbnService[0] = IZxcvbnService.Stub.asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mZxcvbnService[0] = null
            }
        }
        ctx.bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE)

        val inflater = LayoutInflater.from(ctx)
        val view = inflater.inflate(R.layout.new_password_input_dialog, null)

        val builder = AlertDialog.Builder(ctx)
            .setView(view)
            .setPositiveButton(getPositiveText(mode), null) // Set to null. We override the listener later.
            .setNegativeButton(R.string.common_cancel) { dialog, _ ->
                val etPw1 = view.findViewById<EditText>(R.id.new_password_password)
                val etPw2 = view.findViewById<EditText>(R.id.new_password_password_again)
                etPw1.setText("")
                etPw2.setText("")
                callback.neutralAction()
                dialog.dismiss()
            }
            .setOnCancelListener {
                callback.neutralAction()
            }
            .setOnDismissListener {
                try {
                    mZxcvbnService[0]?.exit()
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
                ctx.unbindService(mConnection)
            }

        val dialog = builder.create()

        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val etPw1 = view.findViewById<EditTextPasswordWithVisibilityToggle>(R.id.new_password_password)
        val etPw2 = view.findViewById<EditTextPasswordWithVisibilityToggle>(R.id.new_password_password_again)

        etPw2.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (handlePositive(view, callback, mode, mZxcvbnService[0])) {
                    dialog.dismiss()
                    return@setOnEditorActionListener true
                }
            }
            false
        }

        val wrapPw1 = view.findViewById<TextInputLayout>(R.id.new_password_password_wrapper)
        val tvText = view.findViewById<TextView>(R.id.new_password_text)
        tvText.setText(getBody(mode))

        val tvTitle = view.findViewById<TextView>(R.id.new_password_title)
        tvTitle.setText(getTitle(mode))

        val cbWeak = view.findViewById<CheckBox>(R.id.cb_accept_weak_password)
        cbWeak.visibility = View.GONE

        val btSuggest = view.findViewById<Button>(R.id.new_password_generate)
        btSuggest.setOnClickListener {
            try {
                val dw = DiceWare(ctx).getDiceWords(
                    getDicewareExtraSecurity(mode)!!,
                    getDicewareNumWords(mode)
                )
                etPw1.setText(dw.toString())
                etPw1.setPasswordVisible(true)
                etPw2.setPasswordVisible(true)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val sbStrength = view.findViewById<SeekBar>(R.id.create_key_seekbar)
        etPw1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val entropy = calcPasswordEntropy(s, wrapPw1, mZxcvbnService[0])
                updateSeekBar(view, sbStrength, entropy, mode)
            }
            override fun afterTextChanged(s: Editable) {}
        })

        updateSeekBar(view, sbStrength, 0, mode)

        // Override the positive button listener to prevent auto-dismissal
        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                if (handlePositive(view, callback, mode, mZxcvbnService[0])) {
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }


    private fun getBody(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> R.string.new_password_keystore_text
            NewPasswordInputDialog.MODE.PBKDF -> R.string.new_password_pbkdf_text
            NewPasswordInputDialog.MODE.SHARE -> R.string.new_password_share_text
        }
    }

    private fun getTitle(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> R.string.new_password_keystore_title
            NewPasswordInputDialog.MODE.PBKDF -> R.string.new_password_pbkdf_title
            NewPasswordInputDialog.MODE.SHARE -> R.string.new_password_share_title
        }
    }

    private fun getDicewareExtraSecurity(mode: MODE): DiceWare.Type? {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> DiceWare.Type.PASSPHRASE
            NewPasswordInputDialog.MODE.PBKDF -> DiceWare.Type.PASSPHRASE_EXTRA_SECURITY
            NewPasswordInputDialog.MODE.SHARE -> DiceWare.Type.PASSPHRASE_EXTRA_SECURITY
        }
    }

    private fun getDicewareNumWords(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> DICEWARE_WORDS_KEYSTORE
            NewPasswordInputDialog.MODE.PBKDF -> DICEWARE_WORDS_PBKDF
            NewPasswordInputDialog.MODE.SHARE -> DICEWARE_WORDS_SHARE
        }
    }

    private fun getPositiveText(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> R.string.action_save
            NewPasswordInputDialog.MODE.PBKDF -> R.string.action_generate
            NewPasswordInputDialog.MODE.SHARE -> R.string.action_share
        }
    }


    private fun getEntropyHighLevel(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> ENTROPY_HIGH_DEVICE
            NewPasswordInputDialog.MODE.PBKDF -> ENTROPY_HIGH_PBKDF
            NewPasswordInputDialog.MODE.SHARE -> ENTROPY_HIGH_SHARE
        }
    }

    private fun getEntropyMinimum(mode: MODE): Int {
        return when (mode) {
            NewPasswordInputDialog.MODE.KEYSTORE -> ENTROPY_MEDIUM //facilitate usage fur "dumb" users
            NewPasswordInputDialog.MODE.PBKDF -> ENTROPY_MEDIUM //facilitate usage fur "dumb" users
            NewPasswordInputDialog.MODE.SHARE -> ENTROPY_HIGH_SHARE
        }
    }

    private fun updateSeekBar(view: View?, sbStrength: SeekBar, entropy: Int, mode: MODE) {

        sbStrength.max = 100
        sbStrength.progress = Math.max(10, entropy)
        var color = R.color.password_strength_low
        if (entropy >= ENTROPY_MEDIUM) {
            color = R.color.password_strength_medium
        }


        if (entropy >= getEntropyHighLevel(mode)) {
            color = R.color.password_strength_high
            val cbWeak = view!!.findViewById<View>(R.id.cb_accept_weak_password) as CheckBox
            cbWeak.visibility = View.GONE
        }
        sbStrength.progressDrawable.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(sbStrength.context, color),
            PorterDuff.Mode.MULTIPLY
        )


    }


    private fun calcPasswordEntropy(
        s: CharSequence,
        wrapper: TextInputLayout,
        zxcvbn: IZxcvbnService?
    ): Int {
        zxcvbn?: return 0  //service not bound?
        return try {
            val r = zxcvbn.calcEntropy(s.toString())
            wrapper.error = r.warning
            r.entropy
        } catch (ex: RemoteException) {
            ex.printStackTrace()
            0
        }

    }


    private fun handlePositive(
        view: View,
        callback: NewPasswordInputDialogCallback,
        mode: MODE,
        zxcvbn: IZxcvbnService?
    ): Boolean {
        val etPw1 = view.findViewById<View>(R.id.new_password_password) as EditText
        val etPw2 = view.findViewById<View>(R.id.new_password_password_again) as EditText
        val wrapPw1 = view.findViewById<View>(R.id.new_password_password_wrapper) as TextInputLayout
        val wrapPw2 =
            view.findViewById<View>(R.id.new_password_password_again_wrapper) as TextInputLayout
        val cbWeak = view.findViewById<View>(R.id.cb_accept_weak_password) as CheckBox
        val editablePw1 = etPw1.text
        val editablePw2 = etPw2.text

        if (editablePw1.toString() != editablePw2.toString()) {
            wrapPw1.error = view.context.getString(R.string.error_passwords_dont_match)
            return false
        }


        val entropy = calcPasswordEntropy(etPw1.text, wrapPw1, zxcvbn)

        if (entropy < getEntropyMinimum(mode) && !cbWeak.isChecked) {
            wrapPw1.error = view.context.getString(R.string.error_password_length)

            cbWeak.visibility = View.VISIBLE
            cbWeak.requestFocus()
            cbWeak.parent.requestChildFocus(cbWeak, cbWeak)
            return false
        }


        val pl = editablePw1.length
        val aPassPhrase = CharArray(pl)
        editablePw1.getChars(0, pl, aPassPhrase, 0)

        etPw1.setText("") //TODO better way?
        etPw2.setText("") //TODO better way?

        callback.positiveAction(aPassPhrase)


        return true
    }


}
