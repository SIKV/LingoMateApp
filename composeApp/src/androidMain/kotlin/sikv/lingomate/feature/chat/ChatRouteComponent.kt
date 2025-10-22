package sikv.lingomate.feature.chat

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.feature.startchat.StartChatScreen

@Composable
fun ChatRouteComponent(
    onShowNavigationBar: (Boolean) -> Unit = { }
) {
    val backStack = rememberNavBackStack(ChatRoute.StartChat)

    NavDisplay(
        backStack = backStack,
        onBack = {
            repeat(backStack.size - 1) {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == ChatRoute.StartChat)

            entry<ChatRoute.StartChat> {
                StartChatScreen(
                    onNavigateToChat = {
                        backStack.add(ChatRoute.Chat)
                    }
                )
            }
            entry<ChatRoute.Chat> {
                ChatScreen(
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
