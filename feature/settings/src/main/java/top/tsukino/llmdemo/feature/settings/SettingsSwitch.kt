package top.tsukino.llmdemo.feature.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsSwitch(
    vm: SettingsViewModel,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row() {
        Box(
            modifier = Modifier.align(
                Alignment.CenterVertically
            )
        ) {
            SettingsLabel(title)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Switch(
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
                    .padding(
                        horizontal = 16.dp
                    ),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}