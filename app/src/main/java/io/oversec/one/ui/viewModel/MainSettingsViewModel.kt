package io.oversec.one.ui.viewModel

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import io.oversec.one.Core
import io.oversec.one.crypto.gpg.OpenKeychainConnector
import io.oversec.one.db.Db
import io.oversec.one.ui.screen.AppInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainSettingsViewModel(
    private val db: Db,
    private val context: Context
) : ViewModel() {

    private val packageManager: PackageManager = context.packageManager
    private val core: Core = Core.getInstance(context)

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    private val _sortOption = MutableStateFlow("Name")
    val sortOption = _sortOption.asStateFlow()

    private val _isAcsEnabled = MutableStateFlow(core.isAccessibilityServiceRunning)
    val isAcsEnabled = _isAcsEnabled.asStateFlow()

    private val _isBossModeActive = MutableStateFlow(db.isBossMode)
    val isBossModeActive = _isBossModeActive.asStateFlow()

    private val _isOkcInstalled = MutableStateFlow(isPackageInstalled(packageManager, OpenKeychainConnector.PACKAGE_NAME))
    val isOkcInstalled = _isOkcInstalled.asStateFlow()

    init {
        loadApps()
    }

    fun onSortOptionChange(option: String) {
        _sortOption.value = option
    }

    fun onAppCheckedChange(packageName: String, isEnabled: Boolean) {
        viewModelScope.launch {
            db.setAppEnabled(packageName, isEnabled)
            val updatedApps = _apps.value.map {
                if (it.packageName == packageName) it.copy(isEnabled = isEnabled) else it
            }
            _apps.value = updatedApps
        }
    }

    fun refreshStatus() {
        _isAcsEnabled.value = core.isAccessibilityServiceRunning
        _isBossModeActive.value = db.isBossMode
        _isOkcInstalled.value = isPackageInstalled(packageManager, OpenKeychainConnector.PACKAGE_NAME)
    }

    private fun loadApps() {
        viewModelScope.launch {
            _apps.value = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }
                .map {
                    AppInfo(
                        appName = it.loadLabel(packageManager).toString(),
                        packageName = it.packageName,
                        icon = it.loadIcon(packageManager),
                        isEnabled = db.isAppEnabled(it.packageName)
                    )
                }
        }
    }

    private fun isPackageInstalled(pm: PackageManager, packageName: String): Boolean {
        return try {
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}

class MainSettingsViewModelFactory(private val db: Db, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainSettingsViewModel(db, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
