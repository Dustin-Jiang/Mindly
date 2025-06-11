package top.tsukino.mindly.feature.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SheetItem(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
    icon: ImageVector? = null,
    disabled: Boolean = false,
    warning: Boolean = false,
    onClick: () -> Unit
) {
    val color = when {
        warning -> MaterialTheme.colorScheme.error
        disabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        else -> MaterialTheme.colorScheme.onSurface
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !disabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        icon?.let { icon ->
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        CompositionLocalProvider(
            LocalContentColor provides color
        ) {
            text()
        }
    }
}