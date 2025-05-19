package top.tsukino.llmdemo.feature.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultView(
    logo: @Composable () -> Unit,
    text: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .height(96.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            logo()
        }
        Text(text = text)
    }
}

enum class ResultType {
    SUCCESS,
    ERROR,
    EMPTY,
}

val Icon = mapOf<ResultType, String>(
    ResultType.SUCCESS to "\\(^ω^\")/",
    ResultType.ERROR to "(QwQ\")",
    ResultType.EMPTY to "(^-^*)"
)

@Composable
fun ResultView(
    type: ResultType,
    text: String
) {
    ResultView(
        logo = {
            Text(
                text = Icon[type]!!,
                fontSize = 64.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold
            )
        },
        text = text
    )
}