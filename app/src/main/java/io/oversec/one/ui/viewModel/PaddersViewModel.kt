package io.oversec.one.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.oversec.one.db.Db
import io.oversec.one.db.Padder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PaddersViewModel(private val db: Db) : ViewModel() {

    private val _padders = MutableStateFlow<List<Padder>>(emptyList())
    val padders = _padders.asStateFlow()

    init {
        loadPadders()
    }

    fun addPadder(title: String, example: String) {
        viewModelScope.launch {
            val newPadder = Padder()
            newPadder.title = title
            newPadder.example = example
            db.addPadder(newPadder)
            loadPadders()
        }
    }

    fun refreshPadders() {
        loadPadders()
    }

    private fun loadPadders() {
        viewModelScope.launch {
            _padders.value = db.allPadders.toList()
        }
    }
}

class PaddersViewModelFactory(private val db: Db) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaddersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PaddersViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}