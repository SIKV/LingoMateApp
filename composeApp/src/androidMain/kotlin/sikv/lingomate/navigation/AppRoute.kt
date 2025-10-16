package sikv.lingomate.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppRoute : NavKey {

    @Serializable
    data object Chat : AppRoute

    @Serializable
    data object History : AppRoute

    @Serializable
    data object More : AppRoute
}

val bottomNavItems = listOf(
    AppRoute.Chat,
    AppRoute.History,
    AppRoute.More
)
