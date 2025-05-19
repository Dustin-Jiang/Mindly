package top.tsukino.llmdemo.feature.common

import android.util.Log
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

sealed interface NavDest {
    val route: String
    val config: NavDestConfig

    data object Index : NavDest {
        override val route = "index"
        override val config = NavDestConfig.Index
    }

    data class Chat(
        val id: Long
    ) : NavDest {
        override val config = NavDestConfig.Chat
        override val route = config.route
            .replace("{id}", id.toString())
    }
}

fun NavHostController.navigate(dest: NavDest) {
    Log.d("MainController", "navigate: ${dest.route}")
    navigate(dest.route)
}

fun NavHostController.navigate(dest: NavDest, options: NavOptions?) {
    navigate(dest.route, options)
}

fun NavHostController.navigate(dest: NavDest, builder: NavOptionsBuilder.() -> Unit) {
    navigate(dest.route, builder)
}