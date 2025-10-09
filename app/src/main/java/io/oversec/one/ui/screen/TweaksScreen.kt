package io.oversec.one.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.oversec.one.R
import io.oversec.one.db.Db
import io.oversec.one.iab.IabUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ColorsTweakScreen(db: Db, packageName: String, onUpgrade: () -> Unit) {
    val context = LocalContext.current
    var isFullVersion by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        IabUtil.getInstance(context).checkFullVersion { isFullVersion = it }
    }

    var fontSize by remember { mutableStateOf(db.getDecryptOverlayTextSize(packageName).toFloat()) }
    var cornerRadius by remember { mutableStateOf(db.getDecryptOverlayCornerRadius(packageName).toFloat()) }
    var paddingTop by remember { mutableStateOf(db.getDecryptOverlayPaddingTop(packageName).toFloat()) }
    var paddingLeft by remember { mutableStateOf(db.getDecryptOverlayPaddingLeft(packageName).toFloat()) }
    var bgColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color(db.getDecryptOverlayBgColor(packageName))) }
    var fgColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color(db.getDecryptOverlayTextColor(packageName))) }
    var buttonColor by remember { mutableStateOf(androidx.compose.ui.graphics.Color(db.getButtonOverlayBgColor(packageName))) }

    val scope = rememberCoroutineScope()

    var showDialogFor by remember { mutableStateOf<String?>(null) }

    val onColorClick: (String) -> Unit = { colorType ->
        if (isFullVersion) {
            showDialogFor = colorType
        }
    }

    if (showDialogFor != null) {
        val initialColor = when (showDialogFor) {
            "bg" -> bgColor
            "fg" -> fgColor
            "button" -> buttonColor
            else -> androidx.compose.ui.graphics.Color.White
        }

        ColorPickerDialog(
            initialColor = initialColor,
            onColorSelected = { color ->
                when (showDialogFor) {
                    "bg" -> bgColor = color
                    "fg" -> fgColor = color
                    "button" -> buttonColor = color
                }
                showDialogFor = null
            },
            onDismiss = { showDialogFor = null }
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item { SliderRow(stringResource(R.string.font_size), fontSize, { fontSize = it }, 10f..30f) }
        item { SliderRow(stringResource(R.string.corner_radius), cornerRadius, { cornerRadius = it }, 0f..20f) }
        item { SliderRow(stringResource(R.string.padding_top), paddingTop, { paddingTop = it }, 0f..30f) }
        item { SliderRow(stringResource(R.string.padding_left), paddingLeft, { paddingLeft = it }, 0f..30f) }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Text(
                text = stringResource(R.string.lorem),
                fontSize = fontSize.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = paddingLeft.dp, top = paddingTop.dp)
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        if (!isFullVersion) {
            item { Button(onClick = onUpgrade) { Text(stringResource(R.string.action_upgrade)) } }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            Column(modifier = Modifier.alpha(if (isFullVersion) 1f else 0.5f)) {
                Button(onClick = { onColorClick("bg") }) { Text(stringResource(R.string.background_color)) }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onColorClick("fg") }) { Text(stringResource(R.string.font_color)) }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onColorClick("button") }) { Text(stringResource(R.string.button_color)) }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    db.setDecryptOverlayTextSize(packageName, fontSize.toInt())
                    db.setDecryptOverlayCornerRadius(packageName, cornerRadius.toInt())
                    db.setDecryptOverlayPaddingTop(packageName, paddingTop.toInt())
                    db.setDecryptOverlayPaddingLeft(packageName, paddingLeft.toInt())
                    db.setDecryptOverlayBgColor(packageName, bgColor.toArgb())
                    db.setDecryptOverlayTextColor(packageName, fgColor.toArgb())
                    db.setButtonOverlayBgColor(packageName, buttonColor.toArgb())
                }
            }) {
                Text(stringResource(R.string.action_save))
            }
        }
    }
}

@Composable
fun SliderRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = label, modifier = Modifier.weight(1f))
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange, modifier = Modifier.weight(2f))
    }
}

@Composable
fun ColorPickerDialog(
    initialColor: androidx.compose.ui.graphics.Color,
    onColorSelected: (androidx.compose.ui.graphics.Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.select_color)) },
        text = {
            Column {
                HsvColorPicker(
                    modifier = Modifier.fillMaxWidth().height(300.dp),
                    controller = controller,
                    initialColor = initialColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                AlphaSlider(modifier = Modifier.fillMaxWidth(), controller = controller)
                Spacer(modifier = Modifier.height(16.dp))
                BrightnessSlider(modifier = Modifier.fillMaxWidth(), controller = controller)
            }
        },
        confirmButton = {
            TextButton(onClick = { onColorSelected(controller.selectedColor.value) }) { Text(stringResource(R.string.common_ok)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(R.string.common_cancel)) } }
    )
}

@Composable
fun ExpertTweaksScreen(db: Db, packageName: String) {
    val scope = rememberCoroutineScope()
    val prefs = remember {
        mutableStateOf(
            mapOf(
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
        )
    }
    var innerPadding by remember { mutableStateOf(db.getMaxInnerPadding(packageName)) }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infobutton),
                summary = stringResource(R.string.controls_hint_showinfobutton),
                checked = prefs.value["showInfoButton"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showInfoButton", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infoontap),
                summary = stringResource(R.string.controls_hint_infoontap),
                checked = prefs.value["showInfoOnTap"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showInfoOnTap", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infoonlongtap),
                summary = stringResource(R.string.controls_hint_infoonlongtap),
                checked = prefs.value["showInfoOnLongTap"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showInfoOnLongTap", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_encryptbutton),
                summary = stringResource(R.string.controls_hint_showencryptbutton),
                checked = prefs.value["showEncryptButton"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showEncryptButton", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_toggleencryptonlongtap),
                summary = stringResource(R.string.controls_hint_toggleencryptonlongtap),
                checked = prefs.value["toggleEncryptButtonOnLongTap"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("toggleEncryptButtonOnLongTap", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_showuserinteractiondialogsimmediately),
                summary = stringResource(R.string.controls_hint_showuserinteractiondialogsimmediately),
                checked = prefs.value["showUserInteractionDialogsImmediately"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showUserInteractionDialogsImmediately", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_notification),
                summary = stringResource(R.string.controls_hint_shownotification),
                checked = prefs.value["showNotification"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("showNotification", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_overlayaboveinput),
                summary = stringResource(R.string.controls_hint_overlayaboveinput),
                checked = prefs.value["overlayAboveInput"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("overlayAboveInput", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_voverflow),
                summary = stringResource(R.string.controls_hint_voverflow),
                checked = prefs.value["voverflow"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("voverflow", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_newlines),
                summary = stringResource(R.string.controls_hint_newlines),
                checked = prefs.value["appendNewLines"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("appendNewLines", it) } }
            )
        }
        item {
            IntSpinnerPref(
                title = stringResource(R.string.controls_spinner_innerpadding),
                summary = stringResource(R.string.controls_hint_innerpadding),
                value = innerPadding,
                onValueChange = { innerPadding = it },
                values = listOf(0, 8, 32, 128, 512)
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_storeencryptionparamsperpackageonly),
                summary = stringResource(R.string.controls_hint_storeencryptionparamsperpackageonly),
                checked = prefs.value["storeEncryptionParamsPerPackageOnly"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("storeEncryptionParamsPerPackageOnly", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_forceencryptionparams),
                summary = stringResource(R.string.controls_hint_forceencryptionparams),
                checked = prefs.value["forceEncryptionParams"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("forceEncryptionParams", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_hqscrape),
                summary = stringResource(R.string.controls_hint_hqscrape),
                checked = prefs.value["hqScrape"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("hqScrape", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_includenotimporantviews),
                summary = stringResource(R.string.controls_hint_includenotimporantviews),
                checked = prefs.value["includeNonImportantViews"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("includeNonImportantViews", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_spreadinvisibleencoding),
                summary = stringResource(R.string.controls_hint_spreadinvisibleencoding),
                checked = prefs.value["spreadInvisibleEncoding"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("spreadInvisibleEncoding", it) } }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_dontshowdecryptionfailed),
                summary = stringResource(R.string.controls_hint_dontshowdecryptionfailed),
                checked = prefs.value["dontShowDecryptionFailed"] ?: false,
                onCheckedChange = { prefs.value = prefs.value.toMutableMap().apply { put("dontShowDecryptionFailed", it) } }
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch(Dispatchers.IO) {
                    prefs.value.forEach { (key, value) ->
                        when (key) {
                            "showInfoButton" -> db.setShowInfoButton(packageName, value as Boolean)
                            "showInfoOnTap" -> db.setShowInfoOnTap(packageName, value as Boolean)
                            "showInfoOnLongTap" -> db.setShowInfoOnLongTap(packageName, value as Boolean)
                            "showEncryptButton" -> db.setShowEncryptButton(packageName, value as Boolean)
                            "toggleEncryptButtonOnLongTap" -> db.setToggleEncryptButtonOnLongTap(packageName, value as Boolean)
                            "showUserInteractionDialogsImmediately" -> db.setShowUserInteractionDialogsImmediately(packageName, value as Boolean)
                            "showNotification" -> db.setShowNotification(packageName, value as Boolean)
                            "overlayAboveInput" -> db.setOverlayAboveInput(packageName, value as Boolean)
                            "voverflow" -> db.setVoverflow(packageName, value as Boolean)
                            "appendNewLines" -> db.setAppendNewLines(packageName, value as Boolean)
                            "storeEncryptionParamsPerPackageOnly" -> db.setStoreEncryptionParamsPerPackageOnly(packageName, value as Boolean)
                            "forceEncryptionParams" -> db.setForceEncryptionParams(packageName, value as Boolean)
                            "hqScrape" -> db.setHqScrape(packageName, value as Boolean)
                            "includeNonImportantViews" -> db.setIncludeNonImportantViews(packageName, value as Boolean)
                            "spreadInvisibleEncoding" -> db.setSpreadInvisibleEncoding(packageName, value as Boolean)
                            "dontShowDecryptionFailed" -> db.setDontShowDecryptionFailed(packageName, value as Boolean)
                        }
                    }
                    db.setMaxInnerPadding(packageName, innerPadding)
                }
            }) {
                Text(stringResource(R.string.action_save))
            }
        }
    }
}

@Composable
fun CheckboxPref(
    title: String,
    summary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onCheckedChange(!checked) }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun IntSpinnerPref(
    title: String,
    summary: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    values: List<Int>
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().clickable { expanded = true }.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodyMedium)
        }
        Box {
            Text(text = value.toString())
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                values.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.toString()) },
                        onClick = {
                            onValueChange(it)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}