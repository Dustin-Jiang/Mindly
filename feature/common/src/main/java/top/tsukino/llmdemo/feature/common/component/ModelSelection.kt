package top.tsukino.llmdemo.feature.common.component

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import top.tsukino.llmdemo.data.database.entity.ModelEntity

@Composable
fun ModelSelection(
    modelList: List<ModelEntity>,
    selectedModel: ModelEntity?,
    onSelectModel: (ModelEntity) -> Unit,
) {
    val showSelectDialog = remember { mutableStateOf(false) }
    InputChip(
        enabled = modelList.isNotEmpty(),
        selected = selectedModel != null,
        onClick = { showSelectDialog.value = true },
        label = {
            Text(
                text = selectedModel?.modelId ?: "选择模型"
            )
        },
        trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "Select a model") }
    )

    when {
        showSelectDialog.value -> ModelSelectDialog(
            modelList = modelList,
            onDismiss = { showSelectDialog.value = false },
            onSelect = { model ->
                showSelectDialog.value = false
                onSelectModel(
                    model
                )
                Log.d(
                    "InputBar",
                    "Model selected: $model"
                )
            },
            selected = selectedModel
        )
    }
}