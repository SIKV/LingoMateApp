package sikv.lingomate.feature.more

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MoreScreen() {
    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "More",
                modifier = Modifier
                    .padding(padding)
                    .align(Alignment.Center)
            )
        }
    }
}
