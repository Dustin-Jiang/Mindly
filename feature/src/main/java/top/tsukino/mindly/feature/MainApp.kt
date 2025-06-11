package top.tsukino.mindly.feature

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import top.tsukino.mindly.feature.chat.ChatScreen
import top.tsukino.mindly.feature.common.MainController
import top.tsukino.mindly.feature.common.NavAnimation
import top.tsukino.mindly.feature.common.NavDestConfig
import top.tsukino.mindly.feature.common.composableChat
import top.tsukino.mindly.feature.common.composableIndex
import top.tsukino.mindly.feature.common.enterTransition
import top.tsukino.mindly.feature.common.exitTransition
import top.tsukino.mindly.feature.common.popEnterTransition
import top.tsukino.mindly.feature.common.popExitTransition
import top.tsukino.mindly.feature.index.IndexScreen
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