package io.oversec.one.crypto.gpg

import android.content.Context

class OpenKeychainConnector {
    companion object {
        const val V_GET_SUBKEY = 1
        fun getInstance(context: Context): OpenKeychainConnector {
            return OpenKeychainConnector()
        }
    }

    fun getVersion(): Int {
        return 0
    }

    fun getAllPackageNames(): List<String> {
        return emptyList()
    }
}
