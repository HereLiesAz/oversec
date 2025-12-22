package io.oversec.one.view.util

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import io.oversec.one.R
import org.openintents.openpgp.OpenPgpSignatureResult
import org.openintents.openpgp.util.OpenPgpApi

object GpgCryptoUIUtil {
    const val PACKAGE_NAME_KEYCHAIN = "org.sufficientlysecure.keychain"

    fun openOpenKeyChain(ctx: Context) {
        try {
            val i = Intent(Intent.ACTION_MAIN)
            i.component = ComponentName(
                PACKAGE_NAME_KEYCHAIN,
                "org.sufficientlysecure.keychain.ui.MainActivity"
            )
            i.setPackage(PACKAGE_NAME_KEYCHAIN)
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            if (ctx !is Activity) {
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            ctx.startActivity(i)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun signatureResultToUiText(ctx: Context, sr: OpenPgpSignatureResult): String? {
        return when (sr.result) {
            OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE -> ctx.getString(R.string.signature_result__invalid_insecure)
            OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED -> ctx.getString(R.string.signature_result__invalid_key_expired)
            OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED -> ctx.getString(R.string.signature_result__invalid_key_revoked)
            OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE -> ctx.getString(R.string.signature_result__invalid)
            OpenPgpSignatureResult.RESULT_KEY_MISSING -> ctx.getString(R.string.signature_result__key_missing)
            OpenPgpSignatureResult.RESULT_NO_SIGNATURE -> ctx.getString(R.string.signature_result__no_signature)
            OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED -> ctx.getString(R.string.signature_result__valid_confirmed)
            OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED -> ctx.getString(R.string.signature_result__valid_unconfirmed)
            else -> null
        }
    }

    fun signatureResultToUiColorResId(sr: OpenPgpSignatureResult): Int {
        return when (sr.result) {
            OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE, OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED, OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED -> R.color.colorWarning
            OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE, OpenPgpSignatureResult.RESULT_KEY_MISSING, OpenPgpSignatureResult.RESULT_NO_SIGNATURE -> R.color.colorError
            OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED, OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED -> R.color.colorOk
            else -> 0
        }
    }

    fun signatureResultToUiIconRes(sr: OpenPgpSignatureResult, small: Boolean): Int {
        return when (sr.result) {
            OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE, OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED, OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED, OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE -> if (small) R.drawable.ic_error_red_18dp else R.drawable.ic_error_red_24dp
            OpenPgpSignatureResult.RESULT_NO_SIGNATURE -> if (small) R.drawable.ic_warning_red_18dp else R.drawable.ic_warning_red_24dp
            OpenPgpSignatureResult.RESULT_KEY_MISSING -> if (small) R.drawable.ic_warning_orange_18dp else R.drawable.ic_warning_orange_24dp
            OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED -> if (small) R.drawable.ic_done_orange_18dp else R.drawable.ic_done_orange_24dp
            OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED -> if (small) R.drawable.ic_done_all_green_a700_18dp else R.drawable.ic_done_all_green_a700_24dp
            else -> 0
        }
    }

    fun signatureResultToUiColorResId_KeyOnly(sr: OpenPgpSignatureResult): Int {
        return when (sr.result) {
            OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE, OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED, OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED, OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE, OpenPgpSignatureResult.RESULT_KEY_MISSING, OpenPgpSignatureResult.RESULT_NO_SIGNATURE, OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED -> R.color.colorWarning
            OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED -> R.color.colorOk
            else -> 0
        }
    }

    fun signatureResultToUiIconRes_KeyOnly(sr: OpenPgpSignatureResult, small: Boolean): Int {
        return when (sr.result) {
            OpenPgpSignatureResult.RESULT_INVALID_KEY_INSECURE, OpenPgpSignatureResult.RESULT_INVALID_KEY_EXPIRED, OpenPgpSignatureResult.RESULT_INVALID_KEY_REVOKED, OpenPgpSignatureResult.RESULT_INVALID_SIGNATURE, OpenPgpSignatureResult.RESULT_NO_SIGNATURE, OpenPgpSignatureResult.RESULT_KEY_MISSING, OpenPgpSignatureResult.RESULT_VALID_KEY_UNCONFIRMED -> if (small) R.drawable.ic_warning_red_18dp else R.drawable.ic_warning_red_24dp
            OpenPgpSignatureResult.RESULT_VALID_KEY_CONFIRMED -> if (small) R.drawable.ic_done_green_a700_18dp else R.drawable.ic_done_green_a700_24dp
            else -> 0
        }
    }
}
