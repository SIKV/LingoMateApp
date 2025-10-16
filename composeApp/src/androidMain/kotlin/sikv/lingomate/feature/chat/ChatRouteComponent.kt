package sikv.lingomate.feature.chat

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.feature.chatdetails.ChatDetailsScreen

@Composable
fun ChatRouteComponent(
    onShowNavigationBar: (Boolean) -> Unit = { }
) {
    val backStack = rememberNavBackStack(ChatRoute.Root)

    NavDisplay(
        backStack = backStack,
        onBack = {
            repeat(backStack.size - 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == ChatRoute.Root)

            entry<ChatRoute.Root> {
                ChatScreen(
                    onNavigateToChatDetails = {
                        backStack.add(ChatRoute.ChatDetails)
                    }
                )
            }
            entry<ChatRoute.ChatDetails> {
                ChatDetailsScreen(
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
