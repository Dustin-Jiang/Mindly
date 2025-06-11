package top.tsukino.mindly.feature.settings.model

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import top.tsukino.mindly.feature.settings.SettingsLabel
import top.tsukino.mindly.feature.settings.SettingsSwitch
import top.tsukino.mindly.feature.settings.SettingsViewModel

fun LazyListScope.UserExperience(
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
            val taskModelName by vm.taskModelName.collectAsState()
            val enableSummaryTitle by vm.enableSummaryTitle.collectAsState()
            SettingsSwitch(
                title = "使用任务模型总结对话标题",
                checked = enableSummaryTitle,
                disabled = taskModelName.isEmpty(),
                onCheckedChange = {
                    vm.updateEnableSummaryTitle(
                        it
                    )
                }
            )
        }
    }
    item(
        key = "ImmediateTranscript"
    ) {
        Box {
            val sttModelName by vm.sttModelName.collectAsState()
            val immediateTranscript by vm.immediateTranscript.collectAsState()
            SettingsSwitch(
                title = "实时转写语音",
                checked = immediateTranscript,
                disabled = sttModelName.isEmpty(),
                onCheckedChange = {
                    vm.updateImmediateTranscript(
                        it
                    )
                }
            )
        }
    }
}