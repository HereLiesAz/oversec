package io.oversec.one.crypto.proto

import com.google.protobuf.ByteString
import java.io.InputStream

object Outer {
    class Msg {
        val msgDataCase: MsgDataCase = MsgDataCase.MSGDATA_NOT_SET
        fun hasMsgTextGpgV0(): Boolean {
            return false
        }
        val msgTextGpgV0: Any? = null


        enum class MsgDataCase {
            MSGDATA_NOT_SET
        }

        companion object {
            fun parseDelimitedFrom(bis: InputStream): Msg {
                return Msg()
            }
        }

        fun writeDelimitedTo(baos: java.io.ByteArrayOutputStream) {
        }
    }

    class InnerData {
        val imageV0: ImageV0 = ImageV0()
        companion object {
            fun getDefaultInstance(): InnerData {
                return InnerData()
            }
        }
    }

    class ImageV0 {
        val image: ByteString = ByteString.EMPTY
    }
}
