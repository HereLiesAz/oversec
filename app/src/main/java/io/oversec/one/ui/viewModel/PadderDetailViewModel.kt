package io.oversec.one.ui.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.oversec.one.common.CoreContract
import io.oversec.one.crypto.encoding.pad.PadderContent
import io.oversec.one.db.PadderDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PadderDetailViewModel(
    private val db: PadderDb,
    private val padderId: Long?
) : ViewModel() {

    private val _padder = MutableStateFlow<PadderContent?>(null)
    val padder = _padder.asStateFlow()

    init {
        padderId?.let {
            viewModelScope.launch {
                _padder.value = db.get(it)
            }
        }
    }

    fun savePadder(name: String, content: String, activity: Activity, afterSave: () -> Unit) {
        CoreContract.instance.doIfFullVersionOrShowPurchaseDialog(activity, {
            viewModelScope.launch {
                val pc = _padder.value
                if (pc == null) {
                    val newPadder = PadderContent(name, content)
                    db.add(newPadder)
                } else {
                    pc.name = name
                    pc.content = content
                    pc.setSort(name)
                    db.update(pc)
                }
                afterSave()
            }
        }, 9001)
    }

    fun deletePadder(activity: Activity, afterDelete: () -> Unit) {
        CoreContract.instance.doIfFullVersionOrShowPurchaseDialog(activity, {
            viewModelScope.launch {
                _padder.value?.let {
                    db.delete(it.key)
                    afterDelete()
                }
            }
        }, 9001)
    }
}

class PadderDetailViewModelFactory(private val db: PadderDb, private val padderId: Long?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PadderDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PadderDetailViewModel(db, padderId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
