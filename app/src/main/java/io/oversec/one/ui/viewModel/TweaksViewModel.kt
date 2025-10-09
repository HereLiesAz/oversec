package io.oversec.one.ui.viewModel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.oversec.one.db.Db
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TweaksViewModel(private val db: Db, private val packageName: String) : ViewModel() {

    // ColorsTweakScreen State
    private val _fontSize = MutableStateFlow(db.getDecryptOverlayTextSize(packageName).toFloat())
    val fontSize = _fontSize.asStateFlow()

    private val _cornerRadius = MutableStateFlow(db.getDecryptOverlayCornerRadius(packageName).toFloat())
    val cornerRadius = _cornerRadius.asStateFlow()

    private val _paddingTop = MutableStateFlow(db.getDecryptOverlayPaddingTop(packageName).toFloat())
    val paddingTop = _paddingTop.asStateFlow()

    private val _paddingLeft = MutableStateFlow(db.getDecryptOverlayPaddingLeft(packageName).toFloat())
    val paddingLeft = _paddingLeft.asStateFlow()

    private val _bgColor = MutableStateFlow(Color(db.getDecryptOverlayBgColor(packageName)))
    val bgColor = _bgColor.asStateFlow()

    private val _fgColor = MutableStateFlow(Color(db.getDecryptOverlayTextColor(packageName)))
    val fgColor = _fgColor.asStateFlow()

    private val _buttonColor = MutableStateFlow(Color(db.getButtonOverlayBgColor(packageName)))
    val buttonColor = _buttonColor.asStateFlow()

    // ExpertTweaksScreen State
    private val _expertPrefs = MutableStateFlow(loadExpertPrefs())
    val expertPrefs = _expertPrefs.asStateFlow()

    private val _innerPadding = MutableStateFlow(db.getMaxInnerPadding(packageName))
    val innerPadding = _innerPadding.asStateFlow()

    fun onFontSizeChange(value: Float) { _fontSize.value = value }
    fun onCornerRadiusChange(value: Float) { _cornerRadius.value = value }
    fun onPaddingTopChange(value: Float) { _paddingTop.value = value }
    fun onPaddingLeftChange(value: Float) { _paddingLeft.value = value }
    fun onBgColorChange(color: Color) { _bgColor.value = color }
    fun onFgColorChange(color: Color) { _fgColor.value = color }
    fun onButtonColorChange(color: Color) { _buttonColor.value = color }
    fun onExpertPrefChange(key: String, value: Boolean) {
        val newPrefs = _expertPrefs.value.toMutableMap()
        newPrefs[key] = value
        _expertPrefs.value = newPrefs
    }
    fun onInnerPaddingChange(value: Int) { _innerPadding.value = value }

    fun saveColors() {
        viewModelScope.launch {
            db.setDecryptOverlayTextSize(packageName, _fontSize.value.toInt())
            db.setDecryptOverlayCornerRadius(packageName, _cornerRadius.value.toInt())
            db.setDecryptOverlayPaddingTop(packageName, _paddingTop.value.toInt())
            db.setDecryptOverlayPaddingLeft(packageName, _paddingLeft.value.toInt())
            db.setDecryptOverlayBgColor(packageName, _bgColor.value.toArgb())
            db.setDecryptOverlayTextColor(packageName, _fgColor.value.toArgb())
            db.setButtonOverlayBgColor(packageName, _buttonColor.value.toArgb())
        }
    }

    fun saveExpertTweaks() {
        viewModelScope.launch {
            _expertPrefs.value.forEach { (key, value) ->
                when (key) {
                    "showInfoButton" -> db.setShowInfoButton(packageName, value)
                    "showInfoOnTap" -> db.setShowInfoOnTap(packageName, value)
                    "showInfoOnLongTap" -> db.setShowInfoOnLongTap(packageName, value)
                    "showEncryptButton" -> db.setShowEncryptButton(packageName, value)
                    "toggleEncryptButtonOnLongTap" -> db.setToggleEncryptButtonOnLongTap(packageName, value)
                    "showUserInteractionDialogsImmediately" -> db.setShowUserInteractionDialogsImmediately(packageName, value)
                    "showNotification" -> db.setShowNotification(packageName, value)
                    "overlayAboveInput" -> db.setOverlayAboveInput(packageName, value)
                    "voverflow" -> db.setVoverflow(packageName, value)
                    "appendNewLines" -> db.setAppendNewLines(packageName, value)
                    "storeEncryptionParamsPerPackageOnly" -> db.setStoreEncryptionParamsPerPackageOnly(packageName, value)
                    "forceEncryptionParams" -> db.setForceEncryptionParams(packageName, value)
                    "hqScrape" -> db.setHqScrape(packageName, value)
                    "includeNonImportantViews" -> db.setIncludeNonImportantViews(packageName, value)
                    "spreadInvisibleEncoding" -> db.setSpreadInvisibleEncoding(packageName, value)
                    "dontShowDecryptionFailed" -> db.setDontShowDecryptionFailed(packageName, value)
                }
            }
            db.setMaxInnerPadding(packageName, _innerPadding.value)
        }
    }

    private fun loadExpertPrefs(): Map<String, Boolean> {
        return mapOf(
            "showInfoButton" to db.isShowInfoButton(packageName),
            "showInfoOnTap" to db.isShowInfoOnTap(packageName),
            "showInfoOnLongTap" to db.isShowInfoOnLongTap(packageName),
            "showEncryptButton" to db.isShowEncryptButton(packageName),
            "toggleEncryptButtonOnLongTap" to db.isToggleEncryptButtonOnLongTap(packageName),
            "showUserInteractionDialogsImmediately" to db.isShowUserInteractionDialogsImmediately(packageName),
            "showNotification" to db.isShowNotification(packageName),
            "overlayAboveInput" to db.isOverlayAboveInput(packageName),
            "voverflow" to db.isVoverflow(packageName),
            "appendNewLines" to db.isAppendNewLines(packageName),
            "storeEncryptionParamsPerPackageOnly" to db.isStoreEncryptionParamsPerPackageOnly(packageName),
            "forceEncryptionParams" to db.isForceEncryptionParams(packageName),
            "hqScrape" to db.isHqScrape(packageName),
            "includeNonImportantViews" to db.isIncludeNonImportantViews(packageName),
            "spreadInvisibleEncoding" to db.isSpreadInvisibleEncoding(packageName),
            "dontShowDecryptionFailed" to db.isDontShowDecryptionFailed(packageName)
        )
    }
}

class TweaksViewModelFactory(private val db: Db, private val packageName: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TweaksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TweaksViewModel(db, packageName) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}