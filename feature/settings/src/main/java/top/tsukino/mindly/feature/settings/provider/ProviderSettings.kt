package top.tsukino.mindly.feature.settings.provider

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import top.tsukino.mindly.data.database.entity.ProviderEntity
import top.tsukino.mindly.feature.settings.SettingsLabel
import top.tsukino.mindly.feature.settings.SettingsViewModel

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