package top.tsukino.mindly.feature.chat

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.ModelEntity
import top.tsukino.mindly.feature.common.component.ModelSelection

@Composable
fun InputBar(
    inputValue: InputData,
    modelList: List<ModelEntity>,
    selectedModel: ModelEntity?,
    onSend: () -> Unit = {},
    onInputValueUpdate: (InputData) -> Unit,
    onSelectModel: (ModelEntity) -> Unit
) {
    Log.d("InputBar", "InputBar: $modelList")

    val isAbleToSend = { inputValue.text.isNotEmpty() && selectedModel != null }

    Surface (
        modifier = Modifier.imePadding(),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column (
            modifier = Modifier
                .padding(
                    top = 0.dp,
                    bottom = 12.dp,
                    start = 4.dp,
                    end = 16.dp
                )
                .navigationBarsPadding(),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                ModelSelection(
                    modelList = modelList,
                    selectedModel = selectedModel,
                    onSelectModel = {
                        onSelectModel(it)
                        Log.d("InputBar", "Model selected: $it")
                    }
                )
            }
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = inputValue.text,
                onValueChange = {
                    onInputValueUpdate(
                        inputValue.copy(
                            text = it
                        )
                    )
                },
                placeholder = {
                    Text(
                        "请输入内容"
                    )
                },
                shape = RectangleShape,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                maxLines = 4,
                trailingIcon = {
                    Box {
                        // TODO: 需改为 Button 便于 disable
                        FloatingActionButton(
                            modifier = Modifier
                                .padding(
                                    start = 4.dp
                                )
                                .align(
                                    Alignment.Center
                                ),
                            onClick = {
                                Log.d("InputBar", "send button clicked with ${isAbleToSend()}")
                                if (isAbleToSend()) onSend()
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                "Send Message"
                            )
                        }
                    }
                }
            )
        }
    }
}