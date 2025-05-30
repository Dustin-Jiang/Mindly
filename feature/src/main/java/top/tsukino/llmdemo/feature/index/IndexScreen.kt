package top.tsukino.llmdemo.feature.index

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import top.tsukino.llmdemo.feature.collect.CollectScreen
import top.tsukino.llmdemo.feature.common.MainController
import top.tsukino.llmdemo.feature.common.motion.materialSharedAxisZIn
import top.tsukino.llmdemo.feature.common.motion.materialSharedAxisZOut
import top.tsukino.llmdemo.feature.settings.SettingsScreen
import top.tsukino.llmdemo.home.HomeScreen

/**
 * 主页底部导航栏
 */
@Composable
private fun IndexScreenNavBar(
    pages: List<IndexPage>,
    selectedIndex: String,
    onSelected: (String) -> Unit,
) {
    val currentWindowSize = currentWindowAdaptiveInfo().windowSizeClass
    when (currentWindowSize.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> NavigationBar {
            pages.forEach { page ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = page.label
                        )
                    },
                    label = { Text(text = page.label) },
                    selected = page.route == selectedIndex,
                    onClick = { onSelected(page.route) }
                )
            }
        }
        else -> NavigationRail(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            modifier = Modifier
                .zIndex(1000f)
        ) {
            Spacer(Modifier.weight(1f))
            pages.forEach { page ->
                NavigationRailItem(
                    icon = {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = page.label
                        )
                    },
                    label = { Text(text = page.label) },
                    selected = page.route == selectedIndex,
                    onClick = { onSelected(page.route) }
                )
            }
            Spacer(Modifier.weight(1f))
        }
    }
}

@Composable
private fun IndexScreenLayout(
    mainController: MainController,
    navBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    val currentWindowSize = currentWindowAdaptiveInfo().windowSizeClass
    val paddingValues = WindowInsets.navigationBars.asPaddingValues()

    when (currentWindowSize.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT -> Scaffold(
            bottomBar = navBar,
            snackbarHost = {
                SnackbarHost(
                    hostState = mainController.snackbarHostState
                )
            }
        ) {
            content(it)
        }
        else -> Box {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                navBar()
                content(paddingValues)
            }
            SnackbarHost(
                hostState = mainController.snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun IndexScreen(
    mainController: MainController
) {
    val vm : IndexViewModel = hiltViewModel()
    val navController = rememberNavController()

    val navEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navEntry?.destination?.route
    val startRoute = PageShowOnNav.Home.toString()

    IndexScreenLayout(
        mainController = mainController,
        navBar = {
            IndexScreenNavBar(
                pages = vm.indexScreenConfig.pages,
                selectedIndex = navController.currentDestination?.route ?: vm.indexScreenConfig.pages[vm.indexScreenConfig.initialPage].route,
                onSelected = {
                    if (it == currentRoute) return@IndexScreenNavBar
                    navController.navigate(route = it)
                }
            )
        }
    ) { paddingValues ->
        val bottomPadding = paddingValues.calculateBottomPadding()

        val enterTransition = materialSharedAxisZIn()
        val exitTransition = materialSharedAxisZOut()

        NavHost(
            modifier = Modifier.padding(bottom = bottomPadding),
            navController = navController,
            startDestination = startRoute,
            enterTransition = { enterTransition },
            exitTransition = { exitTransition },
            popEnterTransition = { enterTransition },
            popExitTransition = { exitTransition },
        ) {
            composable(route = PageShowOnNav.Home.toString()) {
                HomeScreen(mainController, navController)
            }
            composable(route = PageShowOnNav.Collect.toString()) {
                CollectScreen(mainController)
            }
            composable(route = PageShowOnNav.Settings.toString()) {
                SettingsScreen(mainController)
            }
        }
    }
}
