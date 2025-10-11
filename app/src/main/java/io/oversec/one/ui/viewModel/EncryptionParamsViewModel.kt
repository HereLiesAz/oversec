package io.oversec.one.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class EncryptionParamsViewModel : ViewModel() {
    var tabIndex by mutableStateOf(0)
        private set

    fun onTabIndexChanged(newIndex: Int) {
        tabIndex = newIndex
    }
}
