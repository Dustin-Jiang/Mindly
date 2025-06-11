package top.tsukino.mindly.feature.common.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.withScope(
    block: suspend () -> Unit
) {
    viewModelScope.launch(Dispatchers.IO) {
        runCatching {
            block()
        }
    }
}