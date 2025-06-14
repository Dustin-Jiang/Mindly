package top.tsukino.mindly.feature.common

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NavDestConfig {
    val route: String
    val arguments: List<NamedNavArgument>

    data object Index : NavDestConfig {
        override val route = "home"
        override val arguments = emptyList<NamedNavArgument>()
    }

    data object Chat : NavDestConfig {
        override val route = "chat/{id}"
        override val arguments = listOf<NamedNavArgument>(
            navArgument("id") {
                type = NavType.LongType
            }
        )
    }

    data object CreateCollectionTextItem : NavDestConfig {
        override val route = "create_collection_text_item"
        override val arguments = emptyList<NamedNavArgument>()
    }
}
