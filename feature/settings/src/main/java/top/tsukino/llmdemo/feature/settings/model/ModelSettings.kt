package top.tsukino.llmdemo.feature.settings.model

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.ModelEntity
import top.tsukino.llmdemo.feature.settings.SettingsLabel
import top.tsukino.llmdemo.feature.settings.SettingsSwitch
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
            vm = vm,
            models = models,
            selectTitle = "选择默认模型",
            model = { vm.defaultModelName.value },
            onSelect = {
                vm.updateDefaultModelId(it.modelId)
            }
        )
    }
    item(key = "SummaryModelNameValue") {
        ModelSelectItem(
            vm = vm,
            models = models,
            selectTitle = "选择任务模型",
            model = { vm.taskModelName.value },
            onSelect = {
                vm.updateTaskModelId(it.modelId)
            }
        )
    }

    if (vm.taskModelName.value.isNotEmpty()) {
        TaskModelSettings (
            vm = vm
        )
    }
}