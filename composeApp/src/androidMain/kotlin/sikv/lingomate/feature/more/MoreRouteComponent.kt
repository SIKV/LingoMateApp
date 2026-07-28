package sikv.lingomate.feature.more

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import sikv.lingomate.feature.manageapikeys.ManageApiKeysScreen

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
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            onShowNavigationBar(backStack.lastOrNull() == MoreRoute.Root)

            entry<MoreRoute.Root> {
                MoreScreen(
                    onNavigateToManageApiKeys = {
                        backStack.add(MoreRoute.ManageApiKeys)
                    }
                )
            }
            entry<MoreRoute.ManageApiKeys> {
                ManageApiKeysScreen(
                    onBackPressed = {
                        backStack.removeLastOrNull()
                    }
                )
            }
        }
    )
}
