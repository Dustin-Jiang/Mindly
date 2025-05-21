package top.tsukino.llmdemo.feature.settings.provider

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.tsukino.llmdemo.data.database.entity.ProviderEntity
import top.tsukino.llmdemo.feature.settings.SettingsLabel
import top.tsukino.llmdemo.feature.settings.SettingsViewModel

fun LazyListScope.ProviderSettings(
    vm: SettingsViewModel,
    providers: List<ProviderEntity>,
) {
    item(key = "ProviderLabel") {
        SettingsLabel("模型提供商")
    }
    if (providers.isEmpty()) {
        item("EmptyProvider") {
            SettingsLabel("没有可用的模型提供商")
        }
    } else {
        items(providers, key = { it.id }) { provider ->
            ProviderItem(vm = vm, provider = provider)
        }
    }
    item(key = "AddProvider") {
        AddProvider(vm = vm)
    }
}