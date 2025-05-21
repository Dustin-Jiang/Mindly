package top.tsukino.llmdemo.feature.settings.model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.feature.settings.SettingsLabel
import top.tsukino.llmdemo.feature.settings.SettingsSwitch
import top.tsukino.llmdemo.feature.settings.SettingsViewModel

fun LazyListScope.TaskModelSettings(
    vm: SettingsViewModel,
) {
    item(
        key = "ConversationExperienceLabel"
    ) {
        SettingsLabel(
            "对话体验"
        )
    }
    item(
        key = "SummaryModelSwitch"
    ) {
        Box {
            val enableSummaryTitle by vm.enableSummaryTitle.collectAsState()
            SettingsSwitch(
                vm = vm,
                title = "使用任务模型总结对话标题",
                checked = enableSummaryTitle,
                onCheckedChange = {
                    vm.updateEnableSummaryTitle(
                        it
                    )
                }
            )
        }
    }
}