package io.oversec.one.view.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun KeystoreTTLDropDown(
    onItemSelected: (Int) -> Unit
) {
    val options = listOf(
        "Lock screen",
        "5 minutes",
        "30 minutes",
        "1 hour",
        "6 hours",
        "1 day",
        "Forever"
    )
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = options[selectedIndex],
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .padding(16.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, text ->
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onItemSelected(index)
                    }
                )
            }
        }
    }
}