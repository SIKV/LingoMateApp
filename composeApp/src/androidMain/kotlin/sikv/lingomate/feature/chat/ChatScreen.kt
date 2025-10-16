package sikv.lingomate.feature.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    onNavigateToChatDetails: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Chat",
                modifier = Modifier.padding(padding)
            )
            Button(
                onClick = {
                    onNavigateToChatDetails()
                }
            ) {
                Text("Navigate to ChatDetails")
            }
        }
    }
}