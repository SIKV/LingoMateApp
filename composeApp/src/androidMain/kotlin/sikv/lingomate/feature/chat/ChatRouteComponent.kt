package sikv.lingomate.feature.chat

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.data.chat.domain.ChatModel
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
                    onNavigateToChat = {
                        backStack.add(ChatRoute.Chat)
                    }
                )
            }
            entry<ChatRoute.Chat> {
                ChatScreen(
                    // TODO: Only for testing.
                    chatLanguage = ChatLanguage.SPANISH,
                    chatLength = ChatLength.MEDIUM_LENGTH,
                    chatModel = ChatModel.GPT_5_MINI,
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
