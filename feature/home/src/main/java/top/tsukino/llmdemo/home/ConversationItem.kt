package top.tsukino.llmdemo.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import top.tsukino.llmdemo.data.database.entity.ConversationEntity
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.NavDest
import top.tsukino.llmdemo.feature.common.utils.DateTimeUtils

@Composable
internal fun ConversationItem(
    mainController: MainController,
    item: ConversationEntity
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                enabled = true,
                onClick = {
                    mainController.navigate(NavDest.Chat(id = item.id))
                }
            )
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = DateTimeUtils.formatReadableTime(item.timestamp) ?: "",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

