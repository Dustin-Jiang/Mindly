package top.tsukino.llmdemo.feature.index

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Archive
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface PageShowOnNav {
    object Home : PageShowOnNav
    object Collect : PageShowOnNav
    object Settings : PageShowOnNav
}

internal data class IndexPage(
    val page: PageShowOnNav,
    val label: String,
    val icon: ImageVector,
) {
    val route = page.toString()
}

internal data class IndexScreenConfig(
    val pages: List<IndexPage>,
    val initialPage: Int,
)

@HiltViewModel
internal class IndexViewModel @Inject constructor() : ViewModel() {
    val indexScreenConfig = IndexScreenConfig(
        pages = listOf(
            IndexPage(PageShowOnNav.Home, label = "对话", icon = Icons.AutoMirrored.Rounded.Chat),
            IndexPage(PageShowOnNav.Collect, label = "收集", icon = Icons.Rounded.Archive),
            IndexPage(PageShowOnNav.Settings, label = "设置", icon = Icons.Rounded.AccountCircle)
        ),
        initialPage = 0,
    )
}