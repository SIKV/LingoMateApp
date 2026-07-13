package sikv.lingomate.feature.chat

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
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
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == ChatRoute.StartChat)

            entry<ChatRoute.StartChat> {
                StartChatScreen(
                    onNavigateToChat = { chatConfig ->
                        backStack.add(ChatRoute.Chat(chatConfig))
                    }
                )
            }
            entry<ChatRoute.Chat> { route ->
                ChatScreen(
                    chatConfig = route.chatConfig,
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
