package io.oversec.one.view.util

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.TextView
import com.google.zxing.BarcodeFormat
import com.jwetherell.quick_response_code.data.Contents
import com.jwetherell.quick_response_code.qrcode.QRCodeEncoder
import io.oversec.one.crypto.sym.SymUtil
import org.bouncycastle.util.encoders.Base64

object SymUIUtil {
    fun getQrCode(data: ByteArray, dimension: Int): Bitmap? {
        try {
            val b64data = Base64.toBase64String(data)

            val qrCodeEncoder = QRCodeEncoder(
                b64data, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                dimension
            )

            return qrCodeEncoder.encodeAsBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun applyAvatar(textView: TextView, name: String?) {
        val hash = name.hashCode()
        val ba = SymUtil.long2bytearray(hash.toLong())
        val red = ((ba[ba.size - 1].toInt() and 0xFF) * 0.8f).toInt()
        val green = ((ba[ba.size - 2].toInt() and 0xFF) * 0.8f).toInt()
        val blue = ((ba[ba.size - 3].toInt() and 0xFF) * 0.8f).toInt()
        val avatarColor = Color.rgb(red, green, blue)
        textView.setBackgroundColor(avatarColor)
        textView.text = (name ?: " ")[0].toString()
    }

    fun applyAvatar(textView: TextView, keyId: Long, name: String) {
        val ba = SymUtil.long2bytearray(keyId)

        val red = ((ba[0].toInt() and 0xFF) * 0.8f).toInt()
        val green = ((ba[1].toInt() and 0xFF) * 0.8f).toInt()
        val blue = ((ba[2].toInt() and 0xFF) * 0.8f).toInt()
        val color = Color.rgb(red, green, blue)

        textView.setBackgroundColor(color)
        textView.text = name[0].toString()
    }
}
