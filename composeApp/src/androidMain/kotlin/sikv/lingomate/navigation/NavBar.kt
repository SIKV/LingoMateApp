package sikv.lingomate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun NavBar(
    currentRoute: AppRoute?,
    onNavigate: (AppRoute) -> Unit
) {
    NavigationBar {
        bottomNavItems.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen,
                onClick = {
                    onNavigate(screen)
                },
                icon = { Icon(screen.icon(), contentDescription = screen.text()) },
                label = { Text(screen.text()) }
            )
        }
    }
}

private fun AppRoute.icon(): ImageVector {
    return when (this) {
        AppRoute.Chat -> Icons.AutoMirrored.Default.Send
        AppRoute.History -> Icons.AutoMirrored.Default.List
        AppRoute.More -> Icons.Default.Menu
    }
}

// TODO: Use string resources.
private fun AppRoute.text(): String {
    return when (this) {
        AppRoute.Chat -> "Chat"
        AppRoute.History -> "History"
        AppRoute.More -> "More"
    }
}
