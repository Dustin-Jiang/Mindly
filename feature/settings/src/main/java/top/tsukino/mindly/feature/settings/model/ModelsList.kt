package top.tsukino.mindly.feature.settings.model

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.ModelEntity
import top.tsukino.mindly.feature.settings.SettingsLabel
import top.tsukino.mindly.feature.settings.SettingsViewModel

fun LazyListScope.ModelsList(
    vm: SettingsViewModel,
    models: List<ModelEntity>,
) {
    // 模型列表部分
    item(key = "ModelsLabel") {
        SettingsLabel("模型列表")
    }
    if (models.isEmpty()) {
        item("EmptyModels") {
            SettingsLabel("没有可用的模型")
        }
    } else {
        Log.d("SettingsScreen", "models: $models")
        items(
            items = models,
            key = { model -> "${model.id}" }
        ) { model ->
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) {
                Text(model.modelId, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}