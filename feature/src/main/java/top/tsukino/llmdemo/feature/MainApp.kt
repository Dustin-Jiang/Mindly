package top.tsukino.llmdemo.feature

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import top.tsukino.llmdemo.feature.chat.ChatScreen
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.NavAnimation
import top.tsukino.llmdemo.feature.common.NavDestConfig
import top.tsukino.llmdemo.feature.common.composableChat
import top.tsukino.llmdemo.feature.common.composableIndex
import top.tsukino.llmdemo.feature.common.enterTransition
import top.tsukino.llmdemo.feature.common.exitTransition
import top.tsukino.llmdemo.feature.common.popEnterTransition
import top.tsukino.llmdemo.feature.common.popExitTransition
import top.tsukino.llmdemo.feature.index.IndexScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun MainApp() {
    val vm : MainAppViewModel = hiltViewModel();
    val ctx = LocalContext.current

    val navController = rememberNavController()

    val controller = MainController(
        scope = rememberCoroutineScope(),
        navController = navController,
        snackbarHostState = remember { SnackbarHostState() },
    )

    val animation = NavAnimation(
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition },
    )

    val systemUiController = rememberSystemUiController()

    val backgroundColor = MaterialTheme.colorScheme.surface
    val bottomNavBarColor = MaterialTheme.colorScheme.surfaceContainerHigh
    val darkTheme = isSystemInDarkTheme()
    LaunchedEffect(bottomNavBarColor, darkTheme) {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )

        systemUiController.setNavigationBarColor(
            color = bottomNavBarColor,
        )
    }

    NavHost(
        navController = navController,
        startDestination = NavDestConfig.Index.route
    ) {
        composableIndex(
            navController = navController,
        ) {
            IndexScreen(mainController = controller)
        }

        composableChat(
            navController = navController,
            navAnimation = animation
        ) { _, id ->
            ChatScreen(mainController = controller, conversationId = id)
        }
    }
}