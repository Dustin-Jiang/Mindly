package top.tsukino.llmdemo.feature.collect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key // Add key import
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.CollectionCategoryEntity

@Composable
fun CategoryList(
    categories: List<CollectionCategoryEntity>,
    selected: Long? = null,
    onChange: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        key(-1L) {
            CategoryItem(
                category = CollectionCategoryEntity(-1L, "全部"),
                isSelected = selected == -1L,
                onChange = { onChange(-1L) }
            )
        }
        categories.forEach { category ->
            key(category.id) {
                CategoryItem(
                    category = category,
                    isSelected = selected == category.id,
                    onChange = { onChange(category.id) }
                )
            }
        }
    }
}

