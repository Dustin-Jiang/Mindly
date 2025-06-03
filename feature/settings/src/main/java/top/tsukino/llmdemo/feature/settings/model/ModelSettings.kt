package top.tsukino.llmdemo.feature.settings.model

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.feature.settings.SettingsLabel
import top.tsukino.llmdemo.feature.settings.SettingsViewModel

fun LazyListScope.ModelSettings(
    vm: SettingsViewModel,
    models: List<ModelEntity>
) {
    item(key = "ModelSettingsLabel") {
        SettingsLabel(
            "模型设置"
        )
    }
    item(key = "DefaultModelNameValue") {
        ModelSelectItem(
            models = models,
            selectTitle = "选择默认模型",
            model = vm.defaultModelName,
            onSelect = {
                vm.updateDefaultModelId(it.modelId)
            }
        )
    }
    item(key = "SummaryModelNameValue") {
        ModelSelectItem(
            models = models,
            selectTitle = "选择任务模型",
            model = vm.taskModelName,
            onSelect = {
                vm.updateTaskModelId(it.modelId)
            }
        )
    }
    item(key = "SttModelNameValue") {
        ModelSelectItem(
            models = models,
            selectTitle = "选择语音识别模型",
            model = vm.sttModelName,
            onSelect = {
                vm.updateSttModelId(it.modelId)
            },
            caption = {
                Text(
                    text = "请确保选择的模型支持语音识别",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        )
    }

    UserExperience (
        vm = vm
    )
}