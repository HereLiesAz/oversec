package io.oversec.one.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import io.oversec.one.R
import io.oversec.one.db.Db
import io.oversec.one.iab.IabUtil
import io.oversec.one.ui.viewModel.TweaksViewModel
import io.oversec.one.ui.viewModel.TweaksViewModelFactory

@Composable
fun ColorsTweakScreen(
    db: Db,
    packageName: String,
    onUpgrade: () -> Unit,
    viewModel: TweaksViewModel = viewModel(factory = TweaksViewModelFactory(db, packageName))
) {
    val context = LocalContext.current
    var isFullVersion by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        IabUtil.getInstance(context).checkFullVersion { isFullVersion = it }
    }

    val fontSize by viewModel.fontSize.collectAsState()
    val cornerRadius by viewModel.cornerRadius.collectAsState()
    val paddingTop by viewModel.paddingTop.collectAsState()
    val paddingLeft by viewModel.paddingLeft.collectAsState()
    val bgColor by viewModel.bgColor.collectAsState()
    val fgColor by viewModel.fgColor.collectAsState()
    val buttonColor by viewModel.buttonColor.collectAsState()

    var showDialogFor by remember { mutableStateOf<String?>(null) }

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
                    "bg" -> viewModel.onBgColorChange(color)
                    "fg" -> viewModel.onFgColorChange(color)
                    "button" -> viewModel.onButtonColorChange(color)
                }
                showDialogFor = null
            },
            onDismiss = { showDialogFor = null }
        )
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item { SliderRow(stringResource(R.string.font_size), fontSize, viewModel::onFontSizeChange, 10f..30f) }
        item { SliderRow(stringResource(R.string.corner_radius), cornerRadius, viewModel::onCornerRadiusChange, 0f..20f) }
        item { SliderRow(stringResource(R.string.padding_top), paddingTop, viewModel::onPaddingTopChange, 0f..30f) }
        item { SliderRow(stringResource(R.string.padding_left), paddingLeft, viewModel::onPaddingLeftChange, 0f..30f) }
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
                Button(onClick = { if(isFullVersion) showDialogFor = "bg" }) { Text(stringResource(R.string.background_color)) }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { if(isFullVersion) showDialogFor = "fg" }) { Text(stringResource(R.string.font_color)) }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { if(isFullVersion) showDialogFor = "button" }) { Text(stringResource(R.string.button_color)) }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.saveColors() }) {
                Text(stringResource(R.string.action_save))
            }
        }
    }
}

@Composable
fun ExpertTweaksScreen(
    db: Db,
    packageName: String,
    viewModel: TweaksViewModel = viewModel(factory = TweaksViewModelFactory(db, packageName))
) {
    val expertPrefs by viewModel.expertPrefs.collectAsState()
    val innerPadding by viewModel.innerPadding.collectAsState()

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        expertPrefs.forEach { (key, checked) ->
            item {
                CheckboxPref(
                    title = stringResource(id = getTitleResId(key)),
                    summary = stringResource(id = getSummaryResId(key)),
                    checked = checked,
                    onCheckedChange = { viewModel.onExpertPrefChange(key, it) }
                )
            }
        }
        item {
            IntSpinnerPref(
                title = stringResource(R.string.controls_spinner_innerpadding),
                summary = stringResource(R.string.controls_hint_innerpadding),
                value = innerPadding,
                onValueChange = viewModel::onInnerPaddingChange,
                values = listOf(0, 8, 32, 128, 512)
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.saveExpertTweaks() }) {
                Text(stringResource(R.string.action_save))
            }
        }
    }
}

private fun getTitleResId(key: String): Int {
    return when (key) {
        "showInfoButton" -> R.string.controls_checkbox_infobutton
        "showInfoOnTap" -> R.string.controls_checkbox_infoontap
        "showInfoOnLongTap" -> R.string.controls_checkbox_infoonlongtap
        "showEncryptButton" -> R.string.controls_checkbox_encryptbutton
        "toggleEncryptButtonOnLongTap" -> R.string.controls_checkbox_toggleencryptonlongtap
        "showUserInteractionDialogsImmediately" -> R.string.controls_checkbox_showuserinteractiondialogsimmediately
        "showNotification" -> R.string.controls_checkbox_notification
        "overlayAboveInput" -> R.string.controls_checkbox_overlayaboveinput
        "voverflow" -> R.string.controls_checkbox_voverflow
        "appendNewLines" -> R.string.controls_checkbox_newlines
        "storeEncryptionParamsPerPackageOnly" -> R.string.controls_checkbox_storeencryptionparamsperpackageonly
        "forceEncryptionParams" -> R.string.controls_checkbox_forceencryptionparams
        "hqScrape" -> R.string.controls_checkbox_hqscrape
        "includeNonImportantViews" -> R.string.controls_checkbox_includenotimporantviews
        "spreadInvisibleEncoding" -> R.string.controls_checkbox_spreadinvisibleencoding
        "dontShowDecryptionFailed" -> R.string.controls_checkbox_dontshowdecryptionfailed
        else -> 0
    }
}

private fun getSummaryResId(key: String): Int {
    return when (key) {
        "showInfoButton" -> R.string.controls_hint_showinfobutton
        "showInfoOnTap" -> R.string.controls_hint_infoontap
        "showInfoOnLongTap" -> R.string.controls_hint_infoonlongtap
        "showEncryptButton" -> R.string.controls_hint_showencryptbutton
        "toggleEncryptButtonOnLongTap" -> R.string.controls_hint_toggleencryptonlongtap
        "showUserInteractionDialogsImmediately" -> R.string.controls_hint_showuserinteractiondialogsimmediately
        "showNotification" -> R.string.controls_hint_shownotification
        "overlayAboveInput" -> R.string.controls_hint_overlayaboveinput
        "voverflow" -> R.string.controls_hint_voverflow
        "appendNewLines" -> R.string.controls_hint_newlines
        "storeEncryptionParamsPerPackageOnly" -> R.string.controls_hint_storeencryptionparamsperpackageonly
        "forceEncryptionParams" -> R.string.controls_hint_forceencryptionparams
        "hqScrape" -> R.string.controls_hint_hqscrape
        "includeNonImportantViews" -> R.string.controls_hint_includenotimporantviews
        "spreadInvisibleEncoding" -> R.string.controls_hint_spreadinvisibleencoding
        "dontShowDecryptionFailed" -> R.string.controls_hint_dontshowdecryptionfailed
        else -> 0
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