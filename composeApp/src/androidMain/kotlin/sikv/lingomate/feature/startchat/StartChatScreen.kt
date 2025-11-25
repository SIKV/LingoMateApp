package sikv.lingomate.feature.startchat

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatLanguage
import sikv.lingomate.data.chat.domain.ChatLength
import sikv.lingomate.feature.toLocalizedString
import sikv.lingomate.ui.isLandscape
import sikv.lingomate.ui.theme.spacing

@Composable
fun StartChatScreen(
    onNavigateToChat: () -> Unit,
    viewModel: StartChatViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLandscape()) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = MaterialTheme.spacing.small)
                    ) {
                        Section(
                            title = stringResource(R.string.start_chat_language_label)
                        ) {
                            ChatLanguageDropdown(
                                onSelect = {
                                    viewModel.selectLanguage(it)
                                },
                                languages = state.chatLanguages,
                                selectedLanguage = state.selectedLanguage,
                                modifier = Modifier.fillMaxWidth(fraction = 0.3f)
                            )
                        }

                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                        Section(
                            title = stringResource(R.string.start_chat_length_label)
                        ) {
                            ChatLengthOptions(
                                onSelect =  {
                                    viewModel.selectLength(it)
                                },
                                lengths = state.chatLengths,
                                selectedLength = state.selectedLength,
                                modifier = Modifier.fillMaxWidth(fraction = 0.7f)
                            )
                        }
                    }

                    StartChatButton(
                        onClick = onNavigateToChat,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.5f)
                            .padding(MaterialTheme.spacing.extraMedium)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Section(
                        title = stringResource(R.string.start_chat_language_label),
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.medium
                            )
                    ) {
                        ChatLanguageDropdown(
                            onSelect = {
                                viewModel.selectLanguage(it)
                            },
                            languages = state.chatLanguages,
                            selectedLanguage = state.selectedLanguage,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Section(
                        title = stringResource(R.string.start_chat_length_label),
                        modifier = Modifier
                            .padding(
                                start = MaterialTheme.spacing.medium,
                                end = MaterialTheme.spacing.medium,
                                bottom = MaterialTheme.spacing.large
                            )
                    ) {
                        ChatLengthOptions(
                            onSelect = {
                                viewModel.selectLength(it)
                            },
                            lengths = state.chatLengths,
                            selectedLength = state.selectedLength
                        )
                    }

                    StartChatButton(
                        onClick = onNavigateToChat,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.spacing.large)
                    )
                }
            }
        }
    }
}

@Composable
private fun Section(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(MaterialTheme.spacing.extraMedium)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        content()
    }
}

@Composable
fun StartChatButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary
    )

    val transition = rememberInfiniteTransition()

    val offsetX by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(offsetX, 0f),
        end = Offset(offsetX + 300f, 300f),
        tileMode = TileMode.Mirror
    )

    Box(
        modifier = modifier
            .height(64.dp)
            .background(brush, shape = RoundedCornerShape(32.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.start_chat_start_button),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        Icon(
            painter = painterResource(R.drawable.ic_auto_awesome_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = MaterialTheme.spacing.extraMedium)
        )
    }
}

@Composable
fun ChatLanguageDropdown(
    onSelect: (ChatLanguage) -> Unit,
    languages: List<ChatLanguage>,
    selectedLanguage: ChatLanguage?,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        if (selectedLanguage != null) {
            FilledTonalButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(selectedLanguage.toLocalizedString())
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.toLocalizedString()) },
                    onClick = {
                        onSelect(language)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ChatLengthOptions(
    onSelect: (ChatLength) -> Unit,
    lengths: List<ChatLength>,
    selectedLength: ChatLength?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            lengths.forEachIndexed { index, length ->
                SegmentedButton(
                    selected = length == selectedLength,
                    onClick = {
                        onSelect(lengths[index])
                    },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = lengths.size)
                ) {
                    Text(length.toLocalizedString())
                }
            }
        }
    }
}
