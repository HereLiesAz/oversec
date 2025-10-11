package io.oversec.one.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.db.PadderDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaddersViewModel(private val db: PadderDb) : ViewModel() {

    private val _padders = MutableStateFlow<List<PadderContent>>(emptyList())
    val padders = _padders.asStateFlow()

    init {
        loadPadders()
    }

    fun addPadder(name: String, content: String) {
        viewModelScope.launch {
            val newPadder = PadderContent(name, content)
            db.add(newPadder)
            loadPadders()
        }
    }

    fun refreshPadders() {
        loadPadders()
    }

    private fun loadPadders() {
        viewModelScope.launch {
            _padders.value = db.allValues
        }
    }
}

class PaddersViewModelFactory(private val db: PadderDb) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaddersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaddersViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
