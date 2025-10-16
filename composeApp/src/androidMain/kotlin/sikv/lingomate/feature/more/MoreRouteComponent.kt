package sikv.lingomate.feature.more

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay

@Composable
fun MoreRouteComponent(
    onShowNavigationBar: (Boolean) -> Unit = { }
) {
    val backStack = rememberNavBackStack(MoreRoute.Root)

    NavDisplay(
        backStack = backStack,
        onBack = {
            repeat(backStack.size - 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == MoreRoute.Root)

            entry<MoreRoute.Root> {
                MoreScreen()
            }
        }
    )
}
