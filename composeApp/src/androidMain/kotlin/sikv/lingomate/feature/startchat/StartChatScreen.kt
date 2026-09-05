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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import sikv.lingomate.R
import sikv.lingomate.data.chat.domain.ChatConfig
import sikv.lingomate.data.chat.domain.Language
import sikv.lingomate.data.chat.domain.PracticeType
import sikv.lingomate.feature.toLocalizedString
import sikv.lingomate.ui.isLandscape
import sikv.lingomate.ui.theme.radius
import sikv.lingomate.ui.theme.spacing

@Composable
fun StartChatScreen(
    onNavigateToChat: (ChatConfig) -> Unit,
    onNavigateToManageApiKeys: () -> Unit,
    viewModel: StartChatViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val chatConfig = state.toChatConfig()

    Scaffold { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isLandscape = isLandscape()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.extraMedium
                    )
            ) {
                val contentModifier = if (isLandscape) {
                    Modifier.fillMaxWidth(fraction = 0.6f)
                } else {
                    Modifier.fillMaxWidth()
                }

                Header(showIcon = !isLandscape)

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraMedium))

                ChatConfigCard(
                    state = state,
                    onNavigateToManageApiKeys = onNavigateToManageApiKeys,
                    onSelectChatModelOption = viewModel::selectChatModel,
                    onSelectPracticeLanguage = viewModel::selectPracticeLanguage,
                    onSelectAssistantLanguage = viewModel::selectAssistantLanguage,
                    onSelectPracticeType = viewModel::selectPracticeType,
                    modifier = contentModifier
                )

                StartChatButton(
                    enabled = chatConfig != null,
                    onClick = { chatConfig?.let(onNavigateToChat) },
                    modifier = contentModifier
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.extraMedium
                        )
                )
            }
        }
    }
}

@Composable
private fun Header(
    showIcon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (showIcon) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_auto_awesome_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        }

        Text(
            text = stringResource(R.string.start_chat_greeting),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        Text(
            text = stringResource(R.string.start_chat_info),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChatConfigCard(
    state: StartChatState,
    onNavigateToManageApiKeys: () -> Unit,
    onSelectChatModelOption: (ChatModelOption) -> Unit,
    onSelectPracticeLanguage: (Language) -> Unit,
    onSelectAssistantLanguage: (Language) -> Unit,
    onSelectPracticeType: (PracticeType) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(MaterialTheme.radius.medium)

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.surfaceDim,
                shape = shape
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = shape
            )
            // Clip the card so the row ripples stay inside its rounded corners.
            .clip(shape)
    ) {
        SelectorRow(
            label = stringResource(R.string.start_chat_chat_model_label),
            options = state.chatModelOptions,
            selected = state.selectedChatModelOption,
            onSelect = onSelectChatModelOption,
            optionLabel = { it.toLocalizedString() },
            optionNote = { option ->
                if (option.apiKeyNeeded) {
                    stringResource(R.string.start_chat_no_api_key)
                } else {
                   null
                }
            },
            optionEnabled = { !it.apiKeyNeeded },
            menuFooter = if (state.chatModelOptions.any { it.apiKeyNeeded }) {
                {
                    MenuHint(
                        text = stringResource(R.string.start_chat_api_key_hint),
                        onClick = onNavigateToManageApiKeys
                    )
                }
            } else {
                null
            }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_practice_language_label),
            options = state.practiceLanguages,
            selected = state.selectedPracticeLanguage,
            onSelect = onSelectPracticeLanguage,
            optionLabel = { it.toLocalizedString() }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_assistant_language_label),
            options = state.assistantLanguages,
            selected = state.selectedAssistantLanguage,
            onSelect = onSelectAssistantLanguage,
            optionLabel = { it.toLocalizedString() }
        )

        SelectorDivider()

        SelectorRow(
            label = stringResource(R.string.start_chat_practice_type_label),
            options = state.practiceTypes,
            selected = state.selectedPracticeType,
            onSelect = onSelectPracticeType,
            optionLabel = { it.toLocalizedString() }
        )
    }
}

@Composable
private fun SelectorDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceDim,
        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraMedium)
    )
}

@Composable
private fun <T : Any> SelectorRow(
    label: String,
    options: List<T>,
    selected: T?,
    onSelect: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    optionNote: @Composable (T) -> String? = { null },
    optionEnabled: (T) -> Boolean = { true },
    menuFooter: @Composable (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = options.isNotEmpty()) { expanded = true }
                .padding(
                    horizontal = MaterialTheme.spacing.extraMedium,
                    vertical = MaterialTheme.spacing.medium
                )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = selected?.let { optionLabel(it) }
                        ?: stringResource(R.string.start_chat_not_selected),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (selected != null) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                val note = optionNote(option)

                DropdownMenuItem(
                    text = { Text(optionLabel(option)) },
                    enabled = optionEnabled(option),
                    trailingIcon = if (note != null) {
                        { OptionNoteBadge(note) }
                    } else if (option == selected) {
                        {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        null
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }

            if (menuFooter != null) {
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceDim)
                menuFooter()
            }
        }
    }
}

@Composable
private fun MenuHint(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxSize()
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.small
            )
    )
}

@Composable
private fun OptionNoteBadge(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = CircleShape
            )
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall
            )
    )
}

private fun StartChatState.toChatConfig(): ChatConfig? {
    return ChatConfig(
        chatModel = selectedChatModelOption?.chatModel ?: return null,
        practiceLanguage = selectedPracticeLanguage ?: return null,
        assistantLanguage = selectedAssistantLanguage ?: return null,
        practiceType = selectedPracticeType ?: return null
    )
}

@Composable
fun StartChatButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(MaterialTheme.radius.extraMedium)
    val glowColor = MaterialTheme.colorScheme.tertiary

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

    val backgroundModifier = if (enabled) {
        Modifier
            // A colored shadow spreading past the edges reads as the button glowing.
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .background(brush, shape = shape)
            // A brighter top edge fading downwards reads as light falling on the button.
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.45f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = shape
            )
    } else {
        Modifier.background(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            shape = shape
        )
    }

    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = modifier
            .height(64.dp)
            .then(backgroundModifier)
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.start_chat_start_button),
            color = contentColor,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        Icon(
            painter = painterResource(R.drawable.ic_auto_awesome_24),
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = MaterialTheme.spacing.extraMedium)
        )
    }
}
