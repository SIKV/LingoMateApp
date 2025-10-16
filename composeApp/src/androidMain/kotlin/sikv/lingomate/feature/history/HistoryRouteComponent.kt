package sikv.lingomate.feature.history

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.feature.chatdetails.ChatDetailsScreen

@Composable
fun HistoryRouteComponent(
    onShowNavigationBar: (Boolean) -> Unit = { }
) {
    val backStack = rememberNavBackStack(HistoryRoute.Root)

    NavDisplay(
        backStack = backStack,
        onBack = {
            repeat(backStack.size - 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == HistoryRoute.Root)

            entry<HistoryRoute.Root> {
                HistoryScreen(
                    onNavigateToChatDetails = {
                        backStack.add(HistoryRoute.ChatDetails)
                    }
                )
            }
            entry<HistoryRoute.ChatDetails> {
                ChatDetailsScreen(
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
