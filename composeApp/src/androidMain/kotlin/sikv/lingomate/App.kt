package sikv.lingomate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import org.jetbrains.compose.ui.tooling.preview.Preview
import sikv.lingomate.feature.chat.ChatRouteComponent
import sikv.lingomate.feature.history.HistoryRouteComponent
import sikv.lingomate.feature.more.MoreRouteComponent
import sikv.lingomate.navigation.AppRoute
import sikv.lingomate.navigation.NavBar
import sikv.lingomate.ui.theme.LocalSpacing
import sikv.lingomate.ui.theme.Spacing

@Composable
@Preview
fun App() {
    val backStack = rememberNavBackStack(AppRoute.Chat)

    val bottomBarVisibleState = remember { mutableStateOf(true) }

    val onShowNavigationBar: (Boolean) -> Unit = { show ->
        bottomBarVisibleState.value = show
    }

    MaterialTheme {
        CompositionLocalProvider(
            LocalSpacing provides Spacing()
        ) {
            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = bottomBarVisibleState.value,
                    ) {
                        NavBar(
                            currentRoute = backStack.lastOrNull() as? AppRoute,
                            onNavigate = { route ->
                                backStack.add(route)
                            }
                        )
                    }
                }
            ) { innerPadding ->
                NavDisplay(
                    backStack = backStack,
                    onBack = {
                        repeat(backStack.size - 1) {
                            backStack.removeLastOrNull()
                        }
                    },
                    entryProvider = entryProvider {
                        entry<AppRoute.Chat> {
                            ChatRouteComponent(onShowNavigationBar = onShowNavigationBar)
                        }
                        entry<AppRoute.History> {
                            HistoryRouteComponent(onShowNavigationBar = onShowNavigationBar)
                        }
                        entry<AppRoute.More> {
                            MoreRouteComponent(onShowNavigationBar = onShowNavigationBar)
                        }
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding)
                )
            }
        }
    }
}
