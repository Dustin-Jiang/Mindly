package top.tsukino.mindly.feature.collect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.mindly.data.database.entity.CollectionCategoryEntity
import top.tsukino.mindly.feature.common.component.CategorySelectForm
import top.tsukino.mindly.feature.common.component.SheetLabel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTextItemForm(
    modifier: Modifier = Modifier,
    content: String,
    title: String,
    selectedCategory: Long?,
    updateContent: (String) -> Unit,
    updateTitle: (String) -> Unit,
    updateSelectedCategory: (Long?) -> Unit,
    categories: List<CollectionCategoryEntity>,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        item {
            SheetLabel(text = "内容")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = content,
                onValueChange = { updateContent(it) },
                minLines = 3,
            )
        }
        item {
            SheetLabel(text = "标题")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                value = title,
                onValueChange = { updateTitle(it) },
            )
        }
        item {
            SheetLabel(text = "选择分类")
        }
        CategorySelectForm(
            categories = categories,
            selected = selectedCategory,
            onSelect = { updateSelectedCategory(it) },
            onCreate = {},
        )
    }
}