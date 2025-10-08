package io.oversec.one.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun ColorsTweakScreen(db: Db, packageName: String, onUpgrade: () -> Unit) {
    val context = LocalContext.current
    var isFullVersion by remember { mutableStateOf(false) }
    IabUtil.getInstance(context).checkFullVersion { isFullVersion = it }

    var fontSize by remember { mutableStateOf(db.getDecryptOverlayTextSize(packageName).toFloat()) }
    var cornerRadius by remember { mutableStateOf(db.getDecryptOverlayCornerRadius(packageName).toFloat()) }
    var paddingTop by remember { mutableStateOf(db.getDecryptOverlayPaddingTop(packageName).toFloat()) }
    var paddingLeft by remember { mutableStateOf(db.getDecryptOverlayPaddingLeft(packageName).toFloat()) }

    var showDialogFor by remember { mutableStateOf<String?>(null) }

    val onColorClick: (String) -> Unit = { colorType ->
        if (isFullVersion) {
            showDialogFor = colorType
        }
    }

    if (showDialogFor != null) {
        val initialColor = when (showDialogFor) {
            "bg" -> androidx.compose.ui.graphics.Color(db.getDecryptOverlayBgColor(packageName))
            "fg" -> androidx.compose.ui.graphics.Color(db.getDecryptOverlayTextColor(packageName))
            "button" -> androidx.compose.ui.graphics.Color(db.getButtonOverlayBgColor(packageName))
            else -> androidx.compose.ui.graphics.Color.White
        }

        ColorPickerDialog(
            initialColor = initialColor,
            onColorSelected = { color ->
                val colorInt = color.toArgb()
                when (showDialogFor) {
                    "bg" -> db.setDecryptOverlayBgColor(packageName, colorInt)
                    "fg" -> db.setDecryptOverlayTextColor(packageName, colorInt)
                    "button" -> db.setButtonOverlayBgColor(packageName, colorInt)
                }
                showDialogFor = null
            },
            onDismiss = { showDialogFor = null }
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            SliderRow(
                label = "Font Size",
                value = fontSize,
                onValueChange = {
                    fontSize = it
                    db.setDecryptOverlayTextSize(packageName, it.toInt())
                },
                valueRange = 10f..30f
            )
        }
        item {
            SliderRow(
                label = "Corner Radius",
                value = cornerRadius,
                onValueChange = {
                    cornerRadius = it
                    db.setDecryptOverlayCornerRadius(packageName, it.toInt())
                },
                valueRange = 0f..20f
            )
        }
        item {
            SliderRow(
                label = "Padding Top",
                value = paddingTop,
                onValueChange = {
                    paddingTop = it
                    db.setDecryptOverlayPaddingTop(packageName, it.toInt())
                },
                valueRange = 0f..30f
            )
        }
        item {
            SliderRow(
                label = "Padding Left",
                value = paddingLeft,
                onValueChange = {
                    paddingLeft = it
                    db.setDecryptOverlayPaddingLeft(packageName, it.toInt())
                },
                valueRange = 0f..30f
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                text = "Lorem Ipsum",
                fontSize = fontSize.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = paddingLeft.dp,
                        top = paddingTop.dp
                    )
            )
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        if (!isFullVersion) {
            item {
                Button(onClick = onUpgrade) {
                    Text("Upgrade")
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Column(modifier = Modifier.alpha(if (isFullVersion) 1f else 0.5f)) {
                Button(onClick = { onColorClick("bg") }) { Text("Background Color") }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onColorClick("fg") }) { Text("Font Color") }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onColorClick("button") }) { Text("Button Color") }
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label, modifier = Modifier.weight(1f))
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.weight(2f)
        )
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
        title = { Text(text = "Select Color") },
        text = {
            Column {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    controller = controller,
                    initialColor = initialColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                AlphaSlider(
                    modifier = Modifier.fillMaxWidth(),
                    controller = controller
                )
                Spacer(modifier = Modifier.height(16.dp))
                BrightnessSlider(
                    modifier = Modifier.fillMaxWidth(),
                    controller = controller
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onColorSelected(controller.selectedColor.value)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ExpertTweaksScreen(db: Db, packageName: String) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infobutton),
                summary = stringResource(R.string.controls_hint_showinfobutton),
                checked = db.isShowInfoButton(packageName),
                onCheckedChange = { db.setShowInfoButton(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infoontap),
                summary = stringResource(R.string.controls_hint_infoontap),
                checked = db.isShowInfoOnTap(packageName),
                onCheckedChange = { db.setShowInfoOnTap(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_infoonlongtap),
                summary = stringResource(R.string.controls_hint_infoonlongtap),
                checked = db.isShowInfoOnLongTap(packageName),
                onCheckedChange = { db.setShowInfoOnLongTap(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_encryptbutton),
                summary = stringResource(R.string.controls_hint_showencryptbutton),
                checked = db.isShowEncryptButton(packageName),
                onCheckedChange = { db.setShowEncryptButton(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_toggleencryptonlongtap),
                summary = stringResource(R.string.controls_hint_toggleencryptonlongtap),
                checked = db.isToggleEncryptButtonOnLongTap(packageName),
                onCheckedChange = { db.setToggleEncryptButtonOnLongTap(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_showuserinteractiondialogsimmediately),
                summary = stringResource(R.string.controls_hint_showuserinteractiondialogsimmediately),
                checked = db.isShowUserInteractionDialogsImmediately(packageName),
                onCheckedChange = { db.setShowUserInteractionDialogsImmediately(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_notification),
                summary = stringResource(R.string.controls_hint_shownotification),
                checked = db.isShowNotification(packageName),
                onCheckedChange = { db.setShowNotification(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_overlayaboveinput),
                summary = stringResource(R.string.controls_hint_overlayaboveinput),
                checked = db.isOverlayAboveInput(packageName),
                onCheckedChange = { db.setOverlayAboveInput(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_voverflow),
                summary = stringResource(R.string.controls_hint_voverflow),
                checked = db.isVoverflow(packageName),
                onCheckedChange = { db.setVoverflow(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_newlines),
                summary = stringResource(R.string.controls_hint_newlines),
                checked = db.isAppendNewLines(packageName),
                onCheckedChange = { db.setAppendNewLines(packageName, it) }
            )
        }
        item {
            IntSpinnerPref(
                title = stringResource(R.string.controls_spinner_innerpadding),
                summary = stringResource(R.string.controls_hint_innerpadding),
                value = db.getMaxInnerPadding(packageName),
                onValueChange = { db.setMaxInnerPadding(packageName, it) },
                values = listOf(0, 8, 32, 128, 512)
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_storeencryptionparamsperpackageonly),
                summary = stringResource(R.string.controls_hint_storeencryptionparamsperpackageonly),
                checked = db.isStoreEncryptionParamsPerPackageOnly(packageName),
                onCheckedChange = { db.setStoreEncryptionParamsPerPackageOnly(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_forceencryptionparams),
                summary = stringResource(R.string.controls_hint_forceencryptionparams),
                checked = db.isForceEncryptionParams(packageName),
                onCheckedChange = { db.setForceEncryptionParams(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_hqscrape),
                summary = stringResource(R.string.controls_hint_hqscrape),
                checked = db.isHqScrape(packageName),
                onCheckedChange = { db.setHqScrape(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_includenotimporantviews),
                summary = stringResource(R.string.controls_hint_includenotimporantviews),
                checked = db.isIncludeNonImportantViews(packageName),
                onCheckedChange = { db.setIncludeNonImportantViews(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_spreadinvisibleencoding),
                summary = stringResource(R.string.controls_hint_spreadinvisibleencoding),
                checked = db.isSpreadInvisibleEncoding(packageName),
                onCheckedChange = { db.setSpreadInvisibleEncoding(packageName, it) }
            )
        }
        item {
            CheckboxPref(
                title = stringResource(R.string.controls_checkbox_dontshowdecryptionfailed),
                summary = stringResource(R.string.controls_hint_dontshowdecryptionfailed),
                checked = db.isDontShowDecryptionFailed(packageName),
                onCheckedChange = { db.setDontShowDecryptionFailed(packageName, it) }
            )
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = summary, style = MaterialTheme.typography.bodyMedium)
        }
        Box {
            Text(text = value.toString())
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
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