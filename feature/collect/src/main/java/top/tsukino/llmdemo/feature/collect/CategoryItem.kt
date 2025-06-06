package top.tsukino.llmdemo.feature.collect

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween // Import tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.CollectionCategoryEntity

@Composable
fun CategoryItem(
    category: CollectionCategoryEntity,
    isSelected: Boolean,
    onChange: () -> Unit,
) {
    val radius by animateDpAsState(
        targetValue = if (isSelected) {
            12.dp
        } else {
            48.dp
        },
        animationSpec = tween(durationMillis = 167),
        label = "CategoryItemRadius"
    )
    val shape = RoundedCornerShape(
        topStart = radius,
        topEnd = radius,
        bottomStart = radius,
        bottomEnd = radius,
    )

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceContainer
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = Modifier
            .clip(shape)
            .clickable(
                onClick = onChange
            ),
        shape = shape,
        color = color,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            text = category.name,
            color = textColor,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

