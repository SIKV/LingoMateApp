package sikv.lingomate.feature.chat

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.feature.chatrecap.ChatRecapScreen
import sikv.lingomate.feature.manageapikeys.ManageApiKeysScreen
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
                    },
                    onNavigateToManageApiKeys = {
                        backStack.add(ChatRoute.ManageApiKeys)
                    }
                )
            }
            entry<ChatRoute.Chat> { route ->
                ChatScreen(
                    chatConfig = route.chatConfig,
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    },
                    onEndChatPressed = { messages ->
                        // The recap takes the chat's place, so leaving it goes back to where the chat was started.
                        backStack[backStack.lastIndex] = ChatRoute.ChatRecap(route.chatConfig, messages)
                    }
                )
            }
            entry<ChatRoute.ChatRecap> { route ->
                ChatRecapScreen(
                    chatConfig = route.chatConfig,
                    messages = route.messages,
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
            entry<ChatRoute.ManageApiKeys> {
                ManageApiKeysScreen(
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
