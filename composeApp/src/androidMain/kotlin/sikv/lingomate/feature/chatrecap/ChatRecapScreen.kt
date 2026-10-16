package sikv.lingomate.feature.chatrecap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.ChatMessage
import sikv.lingomate.data.chat.domain.ChatRecap
import sikv.lingomate.ui.shimmer
import sikv.lingomate.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun ChatRecapScreen(
    onBackPressed: () -> Unit,
    chatConfig: ChatConfig,
    messages: List<ChatMessage>,
    viewModel: ChatRecapViewModel = koinViewModel(
        parameters = { parametersOf(chatConfig, messages) }
    )
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.chat_recap_back_button_content_description)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onBackPressed,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
            ) {
                Text(stringResource(R.string.chat_recap_done_button))
            }
        }
    ) { innerPadding ->
        val recap = state.recap
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when {
            recap != null -> RecapContent(recap = recap, modifier = modifier)
            state.hasFailed -> RecapFailed(onRetryPressed = viewModel::retry, modifier = modifier)
            else -> RecapLoading(modifier = modifier)
        }
    }
}

@Composable
private fun RecapContent(
    recap: ChatRecap,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
        modifier = modifier.padding(MaterialTheme.spacing.extraMedium)
    ) {
        Text(
            text = recap.summary,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// Placeholders in the shape of the recap: a few lines of summary.
@Composable
private fun RecapLoading(modifier: Modifier = Modifier) {
    val loadingDescription = stringResource(R.string.chat_recap_loading)
    val placeholderColor = MaterialTheme.colorScheme.surfaceContainerHighest

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
        modifier = modifier
            .padding(MaterialTheme.spacing.extraMedium)
            .clearAndSetSemantics { contentDescription = loadingDescription }
            .shimmer()
    ) {
        listOf(1f, 0.9f, 0.6f).forEach { widthFraction ->
            Box(
                modifier = Modifier
                    .fillMaxWidth(widthFraction)
                    .height(16.dp)
                    .background(placeholderColor, CircleShape)
            )
        }
    }
}

@Composable
private fun RecapFailed(
    onRetryPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
        modifier = modifier.padding(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.chat_recap_failed),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        OutlinedButton(onClick = onRetryPressed) {
            Text(stringResource(R.string.chat_recap_retry_button))
        }
    }
}
