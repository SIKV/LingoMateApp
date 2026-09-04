package sikv.lingomate.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import sikv.lingomate.R

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

@Composable
private fun AppRoute.icon(): Painter {
    return when (this) {
        // Mirrors the icon in the start chat screen header.
        AppRoute.Chat -> painterResource(R.drawable.ic_auto_awesome_24)
        AppRoute.More -> rememberVectorPainter(Icons.Default.Menu)
    }
}

// TODO: Use string resources.
private fun AppRoute.text(): String {
    return when (this) {
        AppRoute.Chat -> "Practice"
        AppRoute.More -> "More"
    }
}
