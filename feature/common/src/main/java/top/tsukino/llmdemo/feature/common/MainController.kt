package top.tsukino.llmdemo.feature.common

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope

class MainController(
    val scope: CoroutineScope,
    val navController: NavHostController
) {
    fun navigate(dest: NavDest) =
        navController.navigate(dest)

    fun navigate(dest: NavDest, builder: NavOptionsBuilder.() -> Unit) =
        navController.navigate(dest, navOptions(builder))

    fun popBackStack() = navController.popBackStack()
}